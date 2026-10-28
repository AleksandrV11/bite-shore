    function parseWindData(windText) {
        const distantWindMatch = windText.match(/у дальньому проміжку вітер був\s*:\s*([^\n.]+)/i);
        const nearWindMatch = windText.match(/за день до рибалкі вітер був\s*:\s*([^\n.]+)/i);

        const distantWindDirection = distantWindMatch ? distantWindMatch[1].trim() : "Невідомо";
        const nearWindDirection = nearWindMatch ? nearWindMatch[1].trim() : "Невідомо";

        const farWindSpeedMatch = windText.match(
             /А по силі він був\s*([\d.,]+)/i
        );
        const nearWindSpeedMatch = windText.match(
             /А сила його була\s*([\d.,]+)/i
        );
        const farWindStrengthMatch = windText.match(
             /А по силі він був[\s\S]*?Це:\s*([^\n.]+)/i
        );
        const nearWindStrengthMatch = windText.match(
             /А сила його була[\s\S]*?Це:\s*([^\n.]+)/i
        );

        const distantWindSpeed = farWindSpeedMatch
             ? farWindSpeedMatch[1]
               : null;
        const nearWindSpeed = nearWindSpeedMatch
            ? nearWindSpeedMatch[1]
               : null;
        const distantWindStrength = farWindStrengthMatch
            ? farWindStrengthMatch[1]
               : null;
        const nearWindStrength = nearWindStrengthMatch
            ? nearWindStrengthMatch[1]
               : null;

        const distantMatch = windText.match(/кормова база зараз це\s*:\s*([^\n]+)/i);
        const nearMatch = windText.match(/зміщуватись\s*->\s*на\s*:\s*([^\n]+)/i);
        const distantDirection = distantMatch ? distantMatch[1].trim() : null;
        const nearDirection = nearMatch ? nearMatch[1].trim() : null;

        const farStrongWindMatch = windText.match(
            /Стосовно спокою від вітру у дальньму періоді це берег\s*:\s*([^\n.]+)/i
        );
        const nearStrongWindMatch = windText.match(
            /Стосовно спокою від вітру у короткому періоді риба може перемещатись на берег\s*:\s*([^\n.]+)/i
        );

        const farStrongWindDirection = farStrongWindMatch
            ? farStrongWindMatch[1].trim()
               : null;
        const nearStrongWindDirection = nearStrongWindMatch
            ? nearStrongWindMatch[1].trim()
               : null;

       return {
            distantWindDirection: distantWindDirection,
            nearWindDirection: nearWindDirection,
            distantWindSpeed: distantWindSpeed,
            nearWindSpeed: nearWindSpeed,
            distantWindStrength: distantWindStrength,
            nearWindStrength: nearWindStrength,
            distantDirection: distantDirection,
            nearDirection: nearDirection,
            farStrongWindDirection: farStrongWindDirection,
            nearStrongWindDirection: nearStrongWindDirection
       };
    }

    function getWindAngle(direction) {
            const normalized = direction
                .toLowerCase()
                .replace(/\s*-\s*/g, "-")
                .trim();

            const windAngles = {
                   "північний": 0,
                   "північно-східний": 45,
                   "східний": 90,
                   "південно-східний": 135,
                   "південний": 180,
                   "південно-західний": 225,
                   "західний": 270,
                   "північно-західний": 315
            };

       return (windAngles[normalized] + 180) % 360;
    }

    function getOppositeWindDirection(direction) {
           const directions = {
                   "північний": "Південний",
                   "північно-східний": "Південно-західний",
                   "східний": "Західний",
                   "південно-східний": "Північно-західний",
                   "південний": "Північний",
                   "південно-західний": "Північно-східний",
                   "західний": "Східний",
                   "північно-західний": "Південно-східний"
           };

           const normalized = direction
                   .toLowerCase()
                   .replace(/\s*-\s*/g, "-")
                   .trim();

        return directions[normalized] ?? direction;
    }

    function createCompassContainer(windData) {
        const allCompassContainer = document.createElement("div");
        allCompassContainer.className = "all-compasses";

        const distantWindDirection = windData.distantWindDirection;
        const nearWindDirection = windData.nearWindDirection;

        const distantWindSpeed = windData.distantWindSpeed;
        const nearWindSpeed = windData.nearWindSpeed;

        const distantWindStrength = windData.distantWindStrength;
        const nearWindStrength = windData.nearWindStrength;

        const distantDirection = windData.distantDirection;
        const nearDirection = windData.nearDirection;

              allCompassContainer.innerHTML = `
                  <div class="compass-card">
                     <h3>Напрямок вітру</h3>
                     <div class="compass-period">
                        <div class="compass-section">
                            <h4>Дальній період (4–2 дні)</h4>
                            <p>Напрямок: ${getOppositeWindDirection(distantWindDirection)}</p>
                            <p>Швидкість вітру: ${distantWindSpeed} м/с</p>
                            <p>${distantWindStrength}</p>
                               <div class="compass" data-angle="${getWindAngle(distantWindDirection)}">
                                   <span class="north">Пн</span>
                                   <span class="east">Сх</span>
                                   <span class="south">Пд</span>
                                   <span class="west">Зх</span>
                                   <div class="compass-arrow"></div>
                               </div>
                        </div>

                        <div class="compass-section">
                               <h4>Ближній період (1 день)</h4>
                               <p>Напрямок: ${getOppositeWindDirection(nearWindDirection)}</p>
                               <p>Швидкість вітру: ${nearWindSpeed} м/с</p>
                                <p>${nearWindStrength}</p>
                                   <div class="compass" data-angle="${getWindAngle(nearWindDirection)}">
                                       <span class="north">Пн</span>
                                       <span class="east">Сх</span>
                                       <span class="south">Пд</span>
                                       <span class="west">Зх</span>
                                       <div class="compass-arrow"></div>
                                   </div>
                        </div>
                     </div>

                       <h3>Кормова база</h3>

                       <div class="compass-period">
                           <div class="compass-section">
                               <h4>Дальній період (4–2 дні)</h4>
                               <p>Кормова база зараз: ${distantDirection ?? "Невідомо"}</p>
                               <div class="compass" data-direction="${distantDirection ?? ""}">
                                   <span class="north">Пн</span>
                                   <span class="east">Сх</span>
                                   <span class="south">Пд</span>
                                   <span class="west">Зх</span>
                                   <div class="compass-sector"></div>
                               </div>
                           </div>

                           <div class="compass-section">
                               <h4>Ближній період (1 день)</h4>
                               <p>Кормова база може почати зміщуватися на: ${nearDirection ?? "Невідомо"}</p>
                               <div class="compass" data-direction="${nearDirection ?? ""}">
                                   <span class="north">Пн</span>
                                   <span class="east">Сх</span>
                                   <span class="south">Пд</span>
                                   <span class="west">Зх</span>
                                   <div class="compass-sector"></div>
                               </div>
                           </div>
                       </div>
                  </div>
              `;

          return allCompassContainer;
    }
    function createStrongWindContainer(farStrongWindDirection, nearStrongWindDirection) {
             if (farStrongWindDirection || nearStrongWindDirection) {
                   const strongWindContainer = document.createElement("div");
                   strongWindContainer.className = "all-compasses";

                   strongWindContainer.innerHTML = `
                       <div class="compass-card">
                           <h3>Берег зі спокійнішим вітром</h3>
                           <div class="compass-period">
                               ${farStrongWindDirection ? `
                                   <div class="compass-section">
                                       <h4>Дальній період (4–2 дні)</h4>
                                       <p>Напрямок берега: ${farStrongWindDirection}</p>
                                       <div class="compass" data-direction="${farStrongWindDirection}">
                                           <span class="north">Пн</span>
                                           <span class="east">Сх</span>
                                           <span class="south">Пд</span>
                                           <span class="west">Зх</span>
                                           <div class="compass-sector"></div>
                                       </div>
                                   </div>
                               ` : ""}

                               ${nearStrongWindDirection ? `
                                   <div class="compass-section">
                                       <h4>Ближній період (1 день)</h4>
                                       <p>Напрямок берега: ${nearStrongWindDirection}</p>
                                       <div class="compass" data-direction="${nearStrongWindDirection}">
                                           <span class="north">Пн</span>
                                           <span class="east">Сх</span>
                                           <span class="south">Пд</span>
                                           <span class="west">Зх</span>
                                           <div class="compass-sector"></div>
                                       </div>
                                   </div>
                               ` : ""}
                           </div>
                       </div>
                   `;
                     return strongWindContainer;
             }
         return null;
    }
    function highlightCompassDirections(resultElement) {
           // Підсвічуємо напрямок кормової бази
           resultElement.querySelectorAll(".compass[data-direction]").forEach(compass => {
              const direction = compass.dataset.direction
                   .toLowerCase()
                   .trim()
                   .replace(/[.,;:!?]+$/, "")
                   .replace(/\s*-\s*/g, "-");

               const angles = {
                   "північ": 0,
                   "північний": 0,

                   "північний схід": 45,
                   "північно-східний": 45,

                   "схід": 90,
                   "східний": 90,

                   "південний схід": 135,
                   "південно-східний": 135,

                   "південь": 180,
                   "південний": 180,

                   "південний захід": 225,
                   "південно-західний": 225,

                   "захід": 270,
                   "західний": 270,

                   "північний захід": 315,
                   "північно-західний": 315
               };
               const angle = angles[direction];

              if (angle !== undefined) {
                 const sector = compass.querySelector(".compass-sector");

                   if (sector) {
                      sector.style.background = `conic-gradient(
                       from ${angle - 22.5}deg,
                         rgba(40, 230, 100, 0.65) 0deg 45deg,
                           transparent 45deg 360deg
                       )`;
                   }
              }
           });
    }
    function setWindCompassAngles(allCompassContainer) {

        allCompassContainer.querySelectorAll(".compass[data-angle]").forEach(compass => {
            const angle = Number(compass.dataset.angle);
            const arrow = compass.querySelector(".compass-arrow");

            if (Number.isFinite(angle) && arrow) {
                arrow.style.transform = `translateX(-50%) rotate(${angle}deg)`;
            }
        });
    }
    function createWindPreviewCard() {
        const card = document.createElement("div");
        card.className = "wind-card";

        const title = document.createElement("h3");
        title.textContent = "Вітер";
        card.appendChild(title);

        const image = document.createElement("img");
        image.src = "images/wind.svg.webp";
        image.alt = "Вітер";

        card.appendChild(image);

        return card;
    }