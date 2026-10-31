function parseTemperatureData(temperatureText) {

      const upperWaterTemperatureFarPeriodMatch =
          temperatureText.match(/БЕЗ ВПЛИВУ ВІТРА І ОПАДІВ приблизно\s+(\d+)/i);

      const upperWaterTemperatureFarPeriodLevelMatch =
          temperatureText.match(/верхній шар в дальньому періоді має\s+(.+?)\s*\(/i);

      const middleWaterTemperatureFarPeriodMatch =
          temperatureText.match(/середнього шару води в дальньому періоді приблизно\s+(\d+)/i);

      const middleWaterTemperatureFarPeriodLevelMatch =
          temperatureText.match(/середній шар має\s+(.+?)\s*\(/i);

      const upperWaterTemperatureNearPeriodMatch =
          temperatureText.match(/верхнього шару води за день до рибалки приблизно\s+(\d+)/i);

      const upperWaterTemperatureNearPeriodLevelMatch =
          temperatureText.match(/верхнього шару води за день до рибалки приблизно\s+\d+ градусів,\s*тобто верхній шар має\s+(.+?)\s*\(/i);

      const middleWaterTemperatureNearPeriodMatch =
          temperatureText.match(/середнього шару води за день до рибалки приблизно\s+(\d+)/i);

      const middleWaterTemperatureNearPeriodLevelMatch =
        temperatureText.match(/середнього шару води за день до рибалки приблизно\s+\d+ градусів,\s*тобто середній шар за день до рибалки має\s+(.+?)\s*\(/i);

      const notDataNearPeriod = /Данних по температурі за день до рибалки немає/i.test(temperatureText);
      const notDataFarPeriod = /Данних по температурі за 3 дні до рибалки немає/i.test(temperatureText);
      let upperFarRating;
      let middleFarRating;
      let upperNearRating;
      let middleNearRating;

      const temperatureRatings = [
        ...temperatureText.matchAll(/\)\.([^.]*)\./g)
      ].map(match => match[1].trim());

      if (notDataNearPeriod && notDataFarPeriod) {

      }  else if(notDataNearPeriod && !notDataFarPeriod){
          upperFarRating = temperatureRatings[0];
          middleFarRating = temperatureRatings[1];

      }  else if(notDataFarPeriod && !notDataNearPeriod){
          upperNearRating = temperatureRatings[0];
          middleNearRating = temperatureRatings[1];

      }  else {
         upperFarRating = temperatureRatings[0];
         middleFarRating = temperatureRatings[1];
         upperNearRating = temperatureRatings[2];
         middleNearRating = temperatureRatings[3];
      }

      return {
      upperWaterTemperatureFarPeriodMatch: upperWaterTemperatureFarPeriodMatch?.[1],
      upperWaterTemperatureFarPeriodLevelMatch: upperWaterTemperatureFarPeriodLevelMatch?.[1],

      middleWaterTemperatureFarPeriodMatch: middleWaterTemperatureFarPeriodMatch?.[1],
      middleWaterTemperatureFarPeriodLevelMatch: middleWaterTemperatureFarPeriodLevelMatch?.[1],

      upperWaterTemperatureNearPeriodMatch: upperWaterTemperatureNearPeriodMatch?.[1],
      upperWaterTemperatureNearPeriodLevelMatch: upperWaterTemperatureNearPeriodLevelMatch?.[1],

      middleWaterTemperatureNearPeriodMatch: middleWaterTemperatureNearPeriodMatch?.[1],
      middleWaterTemperatureNearPeriodLevelMatch: middleWaterTemperatureNearPeriodLevelMatch?.[1],

      upperFarRating: upperFarRating,
      middleFarRating: middleFarRating,
      upperNearRating: upperNearRating,
      middleNearRating: middleNearRating,

      notDataNearPeriod: Boolean(notDataNearPeriod),
      notDataFarPeriod: Boolean(notDataFarPeriod)
      };
  }
   function createTemperatureCard(temperatureData) {
       const card = document.createElement("div");
       card.className = "temperature-card";

       const waterDiagram = document.createElement("div");
       waterDiagram.className = "water-diagram";

       const farWater = document.createElement("div");
       farWater.className = "far-water";
       const farTitle = document.createElement("h4");
       farTitle.textContent = "Дальній період (за 3 дні до рибалки)";
       farWater.appendChild(farTitle);

       const farUpperLayer = document.createElement("div");
       farUpperLayer.className = "upper-layer";
       const nearTitle = document.createElement("h4");
       nearTitle.textContent = "Ближній період (за день до рибалки)";
       nearWater.appendChild(nearTitle);

       farUpperLayer.textContent = !temperatureData.notDataFarPeriod
           ? `Верхній шар (0 м. - до 1,5 м.): ${temperatureData.upperWaterTemperatureFarPeriodMatch}°C
              має ${temperatureData.upperWaterTemperatureFarPeriodLevelMatch}.
              ${temperatureData.upperFarRating}`
           : "Дані відсутні";

       const farMiddleLayer = document.createElement("div");
       farMiddleLayer.className = "middle-layer";
       farMiddleLayer.textContent = !temperatureData.notDataFarPeriod
           ? `Середній шар (1,5 м. - до 2 м.): ${temperatureData.middleWaterTemperatureFarPeriodMatch}°C
              має ${temperatureData.middleWaterTemperatureFarPeriodLevelMatch}.
              ${temperatureData.middleFarRating}`
           : "Дані відсутні";

       const farBottom = document.createElement("div");
       farBottom.className = "water-bottom";
       farBottom.textContent = "Дно водойми";

       farWater.appendChild(farUpperLayer);
       farWater.appendChild(farMiddleLayer);
       farWater.appendChild(farBottom);

       const nearWater = document.createElement("div");
       nearWater.className = "near-water";

       const nearUpperLayer = document.createElement("div");
       nearUpperLayer.className = "upper-layer";
       nearUpperLayer.textContent = !temperatureData.notDataNearPeriod
           ? `Верхній шар (0 м. - до 1,5 м.): ${temperatureData.upperWaterTemperatureNearPeriodMatch}°C
              має ${temperatureData.upperWaterTemperatureNearPeriodLevelMatch}.
              ${temperatureData.upperNearRating}`
           : "Дані відсутні";

       const nearMiddleLayer = document.createElement("div");
       nearMiddleLayer.className = "middle-layer";
       nearMiddleLayer.textContent = !temperatureData.notDataNearPeriod
           ? `Середній шар (1,5 м. - до 2 м.): ${temperatureData.middleWaterTemperatureNearPeriodMatch}°C
              має ${temperatureData.middleWaterTemperatureNearPeriodLevelMatch}.
              ${temperatureData.middleNearRating}`
           : "Дані відсутні";

       const nearBottom = document.createElement("div");
       nearBottom.className = "water-bottom";
       nearBottom.textContent = "Дно водойми";

       nearWater.appendChild(nearUpperLayer);
       nearWater.appendChild(nearMiddleLayer);
       nearWater.appendChild(nearBottom);

       waterDiagram.appendChild(farWater);
       waterDiagram.appendChild(nearWater);

       const title = document.createElement("h3");
       title.textContent = "Температура води";
       card.appendChild(title);

       card.appendChild(waterDiagram);
      return card;
   }
    function createTemperaturePreviewCard() {
        const card = document.createElement("div");
        card.className = "temperature-card";

        const title = document.createElement("h3");
        title.textContent = "Температура";
        card.appendChild(title);

        const image = document.createElement("img");
        image.src = "images/thermometer.png";
        image.alt = "Термометр";
        image.className = "temperature-icon";

        card.appendChild(image);

        return card;
    }
