# Gif_search_app
This is a GIF search application that allows users to search for GIFs using the Giphy service. It provides an interface to perform searches, view search results, and view detailed information for individual GIFs.

## Technical Details

- **Language:** Kotlin
- **UI:** Jetpack Compose 
- **Architecture Pattern:** MVVM
- **API Documentation:** [Giphy API Documentation](https://developers.giphy.com/docs/api/)


## Getting Started

To run the project, follow these steps:

1. Clone the repository.
2. Important!! Add your own Giphy API key in the gradle.properties under GIPHY_API_KEY
3. Build and run the application on a emulator or a physical device.

## Issues

When writing Unit tests, I encountered an issue when mocking the dependencies for the test.
Specifically the PagingSource.
