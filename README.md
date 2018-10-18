# AvatarView
A circular Image View with a lot of perks. Including progress animation and highlight state with borders and gradient color.

[![Gradle](https://img.shields.io/badge/Version-0.0.1-green.svg)](https://github.com/vitorhugods/AvatarView/releasesgraphs/commit-activity)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-orange.svg)

### Samples
|   |   |
|:-:|:-:|
| <img  src="/pics/ex1.png" alt="Example 1" width="300" style="max-width:100%;"> | <img  src="/pics/ex2.png" alt="Example 1" width="300" style="max-width:100%;"> |
| <img src="/pics/sample1.gif" alt="Example 1" width="300" style="max-width:100%;"> | <img src="/pics/sample2.gif" alt="Example 1" width="300" style="max-width:100%;">  |


## Demo
[Watch the video](https://vimeo.com/291110435) or clone the repo and build the demo app

## Importing to Gradle
Add this to your module's `build.gradle` file
```gradle
repositories {
    maven { url "https://dl.bintray.com/vitorhugods/AvatarView" }
}

dependencies {
    implementation "xyz.schwaab:avvylib:0.0.1"
}
```

## Usage

Just add this to your XML:
```xml
    <xyz.schwaab.avvylib.AvatarView
        android:layout_width="128dp"
        android:layout_height="128dp"
        app:avvy_border_color="@color/grey400"
        app:avvy_border_highlight_color="#ff5900"
        app:avvy_border_highlight_color_end="#bf15bc"
        app:avvy_border_thickness="2dp"
        app:avvy_border_thickness_highlight="3dp"
        app:avvy_distance_to_border="5dp"
        app:avvy_highlighted="true"
        app:avvy_loading_arches="5"
        app:avvy_loading_arches_degree_area="90"/>
```

You can personalize it in Kotlin:
```kotlin
        avatarView.apply {
            isAnimating = false
            borderThickness = 18 //Currently px
            highlightBorderColor = Color.GREEN
            highlightBorderColorEnd = Color.CYAN
            numberOfArches = 0
            totalArchesDegreeArea = 80
        }
```

Or, in Java:
```java
        avatarView.setAnimating(false);
        avatarView.setBorderThickness(18); //Currently px
        avatarView.setHighlightBorderColor(Color.GREEN);
        avatarView.setHighlightBorderColorEnd(Color.CYAN);
        avatarView.setNumberOfArches(0);
        avatarView.setTotalArchesDegreeArea(80);
```


### Special Thanks
The roundness of the drawables based on [Henning Dodenhof's Circle ImageView](https://github.com/hdodenhof/CircleImageView)

 Libraries used in the demo app: 
 - [QuadFlask Color Picker](https://github.com/QuadFlask/colorpicker) 
 - [Bubbleseekbar](https://github.com/woxingxiao/BubbleS...)
 
 

License
-------

    Copyright 2018 Vitor Hugo D. Schwaab

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
