function parsePrecipitationData(precipitationText) {

    const periods = precipitationText.split(
        /За день до рибалки були\s*:/i
    );

    const farText = periods[0].replace(
        /^.*?За 2 дні до рибалки були\s*:/i,
        ""
    ).trim();

    const nearText = periods.length > 1
        ? periods[1].trim()
        : "";

    function parsePeriod(text) {
        if (!text) {
            return {
                precipitation: null,
                recommendation: null
            };
        }

        const match = text.match(/^([^.]*\.?)\s*([\s\S]*)$/);

        return {
            precipitation: match?.[1]?.trim() || null,
            recommendation: match?.[2]?.trim() || null
        };
    }

    const far = parsePeriod(farText);
    const near = parsePeriod(nearText);

    return {
        precipitationFarPeriodMatch: far.precipitation,
        recommendationsFarPrecipitationMatch: far.recommendation,

        precipitationNearPeriodMatch: near.precipitation,
        recommendationsNearPrecipitationMatch: near.recommendation
    };
}
   function createPrecipitationCard(precipitationData) {
       const cardPrecipitation = document.createElement("div");
       cardPrecipitation.className = "precipitation-card";

       const title = document.createElement("h3");
       title.textContent = "Опади";

       const periodsContainer = document.createElement("div");
       periodsContainer.className = "precipitation-periods";

       const farPeriodContainer = document.createElement("div");
       farPeriodContainer.className = "precipitation-period";

       const farTitle = document.createElement("h4");
       farTitle.textContent = "За 2 дні до рибалки";

       const precipitationFarPeriod = document.createElement("div");
       precipitationFarPeriod.className = "precipitation-far";
       precipitationFarPeriod.textContent =
           precipitationData.precipitationFarPeriodMatch ??
           "Опадів не було.";

       const farRecommendation = document.createElement("div");
       farRecommendation.className = "precipitation-recommendation-far";
       farRecommendation.textContent =
           precipitationData.recommendationsFarPrecipitationMatch ??
           "Рекомендацій немає.";

       farPeriodContainer.appendChild(farTitle);
       farPeriodContainer.appendChild(precipitationFarPeriod);
       farPeriodContainer.appendChild(farRecommendation);

       const nearPeriodContainer = document.createElement("div");
       nearPeriodContainer.className = "precipitation-period";

       const nearTitle = document.createElement("h4");
       nearTitle.textContent = "За день до рибалки";

       const precipitationNearPeriod = document.createElement("div");
       precipitationNearPeriod.className = "precipitation-near";
       precipitationNearPeriod.textContent =
           precipitationData.precipitationNearPeriodMatch ??
           "Опадів не було.";

       const nearRecommendation = document.createElement("div");
       nearRecommendation.className = "precipitation-recommendation-near";
       nearRecommendation.textContent =
           precipitationData.recommendationsNearPrecipitationMatch ??
           "Рекомендацій немає.";

       nearPeriodContainer.appendChild(nearTitle);
       nearPeriodContainer.appendChild(precipitationNearPeriod);
       nearPeriodContainer.appendChild(nearRecommendation);

       periodsContainer.appendChild(farPeriodContainer);
       periodsContainer.appendChild(nearPeriodContainer);

       cardPrecipitation.appendChild(title);
       cardPrecipitation.appendChild(periodsContainer);

       return cardPrecipitation;
   }
   function createPrecipitationPreviewCard() {
       const card = document.createElement("div");
       card.className = "precipitation-card";

       const title = document.createElement("h3");
       title.textContent = "Опади";
       card.appendChild(title);

       const image = document.createElement("img");
       image.src = "images/precipitation.png";
       image.alt = "Опади";
       image.className = "precipitation-icon";
       card.appendChild(image);

       return card;
   }

