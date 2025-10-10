# Android News App

This is an Android application developed using **Kotlin**, following **modern Android architecture**
principles. The app fetches news from the [News API](https://newsapi.org/) and allows users to view,
search, and save their favorite news articles.

---

## **Features**

- User registration and login (credentials saved locally using **Room DB**).
- Home screen displaying:
    - **Latest News** (top 5 news articles with "See All" for full headlines)
    - **News Feed** with pagination
    - **Filters** for Health, Technology, Finance, Art, Country, and Category
- Search functionality to find news articles.
- News details screen showing:
    - Image
    - Title
    - Author
    - Description
    - Published date
- Users can **mark news as favorites** (red color if favorite, default otherwise).
- Favorites section to view saved news.
- Profile section to view user details.
- Logout functionality to switch users or login with saved credentials.

---

## **Architecture & Libraries**

- **Architecture:** MVVM (Model-View-ViewModel) with **Clean Architecture** principles
- **Network:** Retrofit with Kotlin **Coroutines** & Flow
- **Local Storage:**
    - **Room DB** for favorite news and user credentials
    - **DataStore** for saving current logged-in user
- **Pagination:** Implemented manuel pagination for Latest News and News Feed
- **UI:**  XML layouts
- **Other Libraries:**
    - Glide for image loading

---

## **Setup Instructions**

1. Clone the repository:
   2.Add you api key in local properties NEWS_API_KEY = YOUR_API_KEY

```bash
git clone <https://github.com/Gaowsik/AssignmentApp>
