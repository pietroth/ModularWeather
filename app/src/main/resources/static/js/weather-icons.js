(() => {
  const iconElement = document.getElementById("weather-icon");
  const descriptionElement = document.getElementById("weather-description");

  if (!iconElement || !descriptionElement) {
    return;
  }

  const normalizedDescription = normalize(descriptionElement.textContent || "");
  const iconClass = resolveWeatherIconClass(normalizedDescription);

  iconElement.classList.forEach((className) => {
    if (className.startsWith("weather-")) {
      iconElement.classList.remove(className);
    }
  });
  iconElement.classList.add(iconClass);

  function normalize(value) {
    return value
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "")
      .toLowerCase()
      .trim();
  }

  function resolveWeatherIconClass(description) {
    if (!description) {
      return "weather-na";
    }

    if (description.includes("thunder") || description.includes("trovo")) {
      return "weather-lightning";
    }

    if (
      description.includes("snow") ||
      description.includes("neve") ||
      description.includes("hail") ||
      description.includes("granizo")
    ) {
      return "weather-cloud-snow";
    }

    if (
      description.includes("rain") ||
      description.includes("chuva") ||
      description.includes("drizzle") ||
      description.includes("garoa")
    ) {
      return "weather-cloud-rain";
    }

    if (
      description.includes("mist") ||
      description.includes("fog") ||
      description.includes("haze") ||
      description.includes("smog") ||
      description.includes("nevoa") ||
      description.includes("fumaca")
    ) {
      return "weather-fog";
    }

    if (description.includes("cloud") || description.includes("nuv")) {
      return "weather-clouds";
    }

    if (
      description.includes("clear") ||
      description.includes("limpo") ||
      description.includes("sun")
    ) {
      return "weather-sun";
    }

    return "weather-na";
  }
})();
