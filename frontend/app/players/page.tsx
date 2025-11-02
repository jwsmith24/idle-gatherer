"use client";

import { useEffect, useState } from "react";

interface Player {
  id: number;
  name: string;
  gold: number;
}

export default function PlayersPage() {
  const [players, setPlayers] = useState<Player[]>([]);

  useEffect(() => {
    (async () => {
      try {
        const response = await fetch("/api/player");
        const data = await response.json();

        setPlayers(data);
      } catch (error) {
        console.error(error);
      }
    })();
  }, []);

  return (
    <main>
      <h1> Players</h1>
      <ul>
        {players.map((p) => (
          <li key={p.id}>
            {p.name} - Gold: {p.gold}
          </li>
        ))}
      </ul>
    </main>
  );
}
