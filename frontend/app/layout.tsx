import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { BannerContextProvider } from "@/app/context/BannerContext";

const inter = Inter({
  variable: "--font-inter",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Idle Gatherer",
  description: "Chop, swing, throw",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${inter.variable} antialiased`}>
        <BannerContextProvider>{children}</BannerContextProvider>
      </body>
    </html>
  );
}
