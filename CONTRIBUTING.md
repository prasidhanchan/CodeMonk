# Contributing

Before reading, you may know what Code Monk is. In short, it's Code Monk is an app designed for the Coding club tah servers as a platform for its members to interact.

For bug reports and feature requests, please search in issues first (including the closed ones). If there're no duplicates, feel free to [submit an issue](https://github.com/prasidhanchan/CodeMonk/issues/new) with an issue template.

Thanks for your interest in contributing to Code Monk! ❤️

# Code contributions

Pull requests are welcome!

If you're interested in taking on [an open issue](https://github.com/prasidhanchan/CodeMonk/issues/), please comment on it.
You need to ask for an assignment so others are aware you are working on it.

## Prerequisites

Before you start, You need to know the following technologies.

- [Android development](https://developer.android.com/)
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/compose)

### Tools

- [Android Studio](https://developer.android.com/studio)


# Forks

- Forks are allowed so long as they abide by [the project's LICENSE](https://github.com/prasidhanchan/CodeMonk/blob/master/LICENSE.md).
- While forking uncheck `copy only the master branch`
  - Contribute your code and make a PR to the dev branch not the master branch.
- Your working branch should have a name like:
  - fix-issue-<issue_number>/<issue_name>
  - feature-<issue_number>/<feature_added>

When working on a feature or a bug, remember:

- To avoid confusion with the main app (Optional):
    - Change the app icon
- To avoid having your data polluting the main app's analytics:
    - Setup Firebase services, place [`google-services.json`](https://github.com/prasidhanchan/CodeMonk/blob/master/app/) with your own.
    - Your json should have additional config for the debug version which will have a different package name.
 
# Pull requests

- Make a pull request to the same branch you made a change i.e dev not the master branch.
