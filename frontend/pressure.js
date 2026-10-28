  function parsePressureData(pressureText) {
      const headPeriodPressureMatch =
          pressureText.match(/періоду перед рибалкою тиск був\s+(\d+)/i);

      const normPressureMatch =
          pressureText.match(/для цієї місцевості\s+(\d+)/i);

      const headPressureLevelMatch =
          headPeriodPressureMatch && normPressureMatch
              ? Number(headPeriodPressureMatch[1]) > Number(normPressureMatch[1])
                  ? "Тиск вищий за норму"
                  : Number(headPeriodPressureMatch[1]) < Number(normPressureMatch[1])
                      ? "Тиск нижчий за норму"
                      : "Тиск у межах норми"
              : null;

      const lastPeriodPressureMatch =
          pressureText.match(/Наприкінці періоду він став\s+(\d+)/i);

      const levelLastDayPressureMatch =
          pressureText.match(/що свідчить про те, що\s*:\s*([^\n.]+)/i);

      const dynamicLastDayPressureMatch =
          pressureText.match(/Зміна тиску протягом останньої доби:\s*([^.]+)/i);

      const notData =
          pressureText.match(/Даних по тиску за день до рибалки немає/i);

      const noPeriodData =
          pressureText.match(/Даних по тиску за аналізований період немає/i);

      return {
          headPressure: headPeriodPressureMatch?.[1],
          normPressure: normPressureMatch?.[1],
          headPressureLevel: headPressureLevelMatch,
          lastPressure: lastPeriodPressureMatch?.[1],
          pressureLevel: levelLastDayPressureMatch?.[1],
          pressureDynamic: dynamicLastDayPressureMatch?.[1],
          notData: Boolean(notData),
          noPeriodData: Boolean(noPeriodData)
      };
  }

 function createPressureCard(pressureData) {
     const card = document.createElement("div");
     card.className = "pressure-card";

     const title = document.createElement("h3");
     title.textContent = "Атмосферний тиск";
     card.appendChild(title);

     const normPressure = document.createElement("p");
     normPressure.textContent =
         `Норма для цієї місцевості: ${pressureData.normPressure} мм рт. ст.`;
     card.appendChild(normPressure);

     const startBlock = document.createElement("div");
     startBlock.className = "start-block";

     const pressureScale = document.createElement("div");
     pressureScale.className = "pressure-scale";

     for (let i = 0; i < 4; i++) {
        const segment = document.createElement("div");
        pressureScale.appendChild(segment);
     }
     card.appendChild(pressureScale);

     const periodPressure = document.createElement("div");
     periodPressure.className = "pressure-period";

     if (pressureData.headPressure) {
         const startPressure = document.createElement("p");
         startPressure.className = "start-pressure";
         startPressure.textContent =
             `Початок періоду: ${pressureData.headPressure} мм рт. ст.`;
         periodPressure.appendChild(startPressure);
     }

     if (pressureData.headPressureLevel) {
         const startPressureLevel = document.createElement("p");
         startPressureLevel.className = "start-pressure";
         startPressureLevel.textContent = pressureData.headPressureLevel;
         periodPressure.appendChild(startPressureLevel);
     }

     if (pressureData.noPeriodData) {
         const noPeriodMessage = document.createElement("p");
         noPeriodMessage.textContent =
             "Даних по тиску за аналізований період немає.";
         noPeriodMessage.className = "no-data";
         periodPressure.appendChild(noPeriodMessage);
     } else if (pressureData.lastPressure) {
         const endPressure = document.createElement("p");
         endPressure.className = "end-pressure";
         endPressure.textContent =
             `Кінець періоду: ${pressureData.lastPressure} мм рт. ст.`;
         periodPressure.appendChild(endPressure);
         if (pressureData.pressureLevel) {
             const endPressureLevel = document.createElement("p");
             endPressureLevel.className = "end-pressure";
             endPressureLevel.textContent = pressureData.pressureLevel;
             periodPressure.appendChild(endPressureLevel);
         }
     }

     if (pressureData.notData) {
         const noLastDayMessage = document.createElement("p");
         noLastDayMessage.textContent =
             "Даних по тиску за день до рибалки немає.";
         noLastDayMessage.className = "no-data";
         periodPressure.appendChild(noLastDayMessage);
     } else if (pressureData.pressureDynamic) {
         const pressureDynamic = document.createElement("p");
         pressureDynamic.className = "pressure-dynamic";
         pressureDynamic.textContent =
             `Зміна тиску за останню добу: ${pressureData.pressureDynamic}`;
         periodPressure.appendChild(pressureDynamic);
     }

     card.appendChild(periodPressure);

     return card;
 }
 function createPressurePreviewCard() {
     const card = document.createElement("div");
     card.className = "pressure-card";

     const title = document.createElement("h3");
     title.textContent = "Тиск";
     card.appendChild(title);

     const image = document.createElement("img");
     image.src = "images/barometer.png";
     image.alt = "Барометр";
     image.className = "pressure-icon";

     card.appendChild(image);

     return card;
 }