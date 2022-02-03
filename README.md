
# File Downloader

**This task helps to download files with the help of Android Download Manager:**

### Architecture
* [Clean Architecture](https://www.amazon.com/Clean-Architecture-Craftsmans-Software-Structure/dp/0134494164)
* [MVVM](https://www.raywenderlich.com/8984-mvvm-on-android)

### Patterns
* [Repository](https://developer.android.com/jetpack/docs/guide)

### Approaches
* [SOLID Principle](https://itnext.io/solid-principles-explanation-and-examples-715b975dcad4?gi=79443348411d)


### Technology Stack
* [Kotlin](https://kotlinlang.org/)
* [View Binding](https://developer.android.com/topic/libraries/view-binding)
* [Dagger 2](https://github.com/google/dagger)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Retrofit 2](https://square.github.io/retrofit/)
* [OkHttp](https://square.github.io/okhttp/)
* [Android Jetpack](https://developer.android.com/jetpack)
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [Dexter](https://github.com/Karumi/Dexter)

### Layers
* core(This layer is responsible to hold interactors (usecases), domain and data ).
* dagger(layer holds everything about dependency injection)
* presentation (Responsible for UI stuff such as displaying data)

