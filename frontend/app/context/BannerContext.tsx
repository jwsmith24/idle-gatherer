"use client";

import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";

export type BannerDisplay = "hidden" | "visible";

interface BannerContextType {
  visibility: BannerDisplay | null;
  toggleVisibility: () => void;
}

const BannerContext = createContext<BannerContextType | undefined>(undefined);

export function BannerContextProvider({ children }: { children: ReactNode }) {
  const [visibility, setVisibility] = useState<BannerDisplay>("hidden");

  useEffect(() => {
    const saved = localStorage.getItem("banner-visibility") as BannerDisplay;
    if (saved) {
      // eslint-disable-next-line react-hooks/set-state-in-effect
      setVisibility(saved);
    }
  }, []);

  useEffect(() => {
    if (visibility === null) return;
    localStorage.setItem("banner-visibility", visibility);
  }, [visibility]);

  const toggleVisibility = () => {
    if (visibility === "visible") {
      setVisibility("hidden");
    } else {
      setVisibility("visible");
    }
  };

  return (
    <BannerContext.Provider value={{ visibility, toggleVisibility }}>
      {children}
    </BannerContext.Provider>
  );
}

export function useBannerContext() {
  const context = useContext(BannerContext);
  if (!context)
    throw new Error("Banner context must be used inside a provider...");
  return context;
}
