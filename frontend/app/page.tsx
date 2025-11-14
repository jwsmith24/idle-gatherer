"use client";
import { useBannerContext } from "@/app/context/BannerContext";

export default function Home() {
  const { visibility, toggleVisibility } = useBannerContext();

  return (
    <div className="flex min-h-screen items-center justify-center font-sans bg-black">
      <main className="flex min-h-screen w-full max-w-3xl flex-col items-center justify-between py-32 px-16 sm:items-start">
        <div className={"bg-blue-500 w-full p-2 rounded-2xl flex gap-2"}>
          <label htmlFor="banner-cb">
            {visibility === "visible"
              ? "Banner is visible"
              : "Banner is hidden"}
          </label>
          <input
            type="checkbox"
            checked={visibility === "visible"}
            onChange={toggleVisibility}
            id={"banner-cb"}
          />
        </div>
      </main>
    </div>
  );
}
