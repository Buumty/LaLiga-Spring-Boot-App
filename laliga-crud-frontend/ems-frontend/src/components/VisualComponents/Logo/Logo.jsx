import React from "react";
import logo from "../../../assets/logo.png";

function Logo({ height = 90, width = 150 }) {
  return (
    <div className="flex items-center gap-2">
      <img
        src={logo}
        alt="LaLiga Fantasy logo"
        width={width}
        height={height}
        className="object-contain"
      />
    </div>
  );
}

export default Logo;