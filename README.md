# LearningApp - Android Application

## Project Overview

The LearningApp is an Android application designed to help users learn various subjects, such as foreign language vocabulary, mathematical formulas, historical dates, and more. Each subject consists of its own set of questions with corresponding answers, managed independently from other subjects.

### Learning Principle

- All questions start at level 1.
- A question is presented to the user. If answered correctly, its status is incremented; if answered incorrectly, its status is reset to level 1.
- Questions at level 1 are reviewed daily, level 2 every two days, level 3 every four days, and so forth.

### Core Features

1. **Creation/Deletion:**
   - Users can create or delete new sets of questions.
   - Users can add or remove questions from a set.

2. **Memory Aid:**
   - Users can choose a set of questions and start/continue learning (answering a series of questions).
   - Users can view the answer to particularly difficult questions.
   - Users can manually set the status of a question or delete a question.
   - Users can view statistics for a specific set of questions or for all sets.

3. **Reminders:**
   - The app sends daily reminders to users via notifications to continue their learning process.

4. **Loading Questions:**
   - Users can download a set of questions from an HTTP address. The file format is JSON.

5. **Customization:**
   - The app’s behavior is customizable, including response time before marking a question wrong, background color, and font size.

6. **Extensions:**
   - Additional features such as questions containing images or sounds have been implemented.

### Technical and Evaluation Criteria

- The project has been developed in Kotlin using Android Studio (Giraffe version).
- The app uses Jetpack Compose (with ViewModel) for the UI and Room for the database.
- Additional technologies used include coroutines, datastore, Material 3 design, screen adaptation, menus, navigation, services, work manager, notifications, download manager, and broadcast receivers.
- Emphasis has been placed on quality programming practices, including application architecture, modularity, code clarity, and database design.
- The application is ergonomic and easy to use, with correct behavior when the screen is rotated.
- While graphical quality is a minor criterion, the app maintains a clean and functional design.

## Getting Started

### Prerequisites

- Android Studio Giraffe or newer
- Kotlin
- Jetpack Compose
- Room Database

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/LearningApp.git
   ```
2. Open the project in Android Studio.
3. Build the project to download necessary dependencies.
4. Run the application on an Android emulator or physical device.
