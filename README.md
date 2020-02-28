# AvatarView
A circular Image View with a lot of perks. Including progress animation and highlight state with borders and gradient color.

[![Build Status](https://travis-ci.com/vitorhugods/AvatarView.svg?branch=master)](https://travis-ci.com/vitorhugods/AvatarView)
[![Gradle](https://img.shields.io/badge/Version-1.0.0-brightgreen.svg)](https://github.com/vitorhugods/AvatarView/releases)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e4847d7f36754c1a8efb1aff838fdb91)](https://app.codacy.com/app/vitorhugods/AvatarView?utm_source=github.com&utm_medium=referral&utm_content=vitorhugods/AvatarView&utm_campaign=Badge_Grade_Dashboard)
![Kotlin](https://img.shields.io/badge/minSdkVersion-14-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-orange.svg)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Say Thanks!](https://img.shields.io/badge/Say%20Thanks-!-1EAEDB.svg)](https://saythanks.io/to/vitorschwaab@outlook.com)

<div align="center">
  <sub>Built with ❤︎ by Vitor Hugo Schwaab and
  <a href="https://github.com/vitorhugods/AvatarView/graphs/contributors">
    contributors
  </a>
</div>
<br/>

### Samples
|   |   |
|:-:|:-:|
| <img  src="/pics/ex1.png" alt="Example 1" width="300" style="max-width:100%;"> | <img  src="/pics/ex2.png" alt="Example 1" width="300" style="max-width:100%;"> |
| <img src="/pics/sample1.gif" alt="Example 1" width="300" style="max-width:100%;"> | <img src="/pics/sample2.gif" alt="Example 1" width="300" style="max-width:100%;">  |


Supports initials if no image is provided:

<img  src="/pics/initials.png" alt="Initials" width="200" style="max-width:100%;">

Thanks to [@anoop44](https://github.com/anoop44)

## Demo
[Watch the video](https://vimeo.com/291110435) or clone the repo and build the demo app

## Importing to Gradle
Add this to your module's `build.gradle` file
```gradle
repositories {
    maven { url "https://dl.bintray.com/vitorhugods/AvatarView" }
}

dependencies {
    implementation "xyz.schwaab:avvylib:1.0.0"
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

Add the name initials as fallback:
```xml
        app:avvy_text="Avatar View" //will show up as AV
        app:avvy_text_size="42sp"
        app:avvy_text_color="#ccc"
```

You can personalize it in Kotlin:
```kotlin
        avatarView.apply {
            isAnimating = false
            borderThickness = 18
            highlightBorderColor = Color.GREEN
            highlightBorderColorEnd = Color.CYAN
            numberOfArches = 0
            totalArchesDegreeArea = 80
            text = "Avatar View"
        }
```

Or, in Java:
```java
        avatarView.setAnimating(false);
        avatarView.setBorderThickness(18);
        avatarView.setHighlightBorderColor(Color.GREEN);
        avatarView.setHighlightBorderColorEnd(Color.CYAN);
        avatarView.setNumberOfArches(0);
        avatarView.setTotalArchesDegreeArea(80);
```


### Special Thanks
The roundness of the drawables based on [Henning Dodenhof's Circle ImageView](https://github.com/hdodenhof/CircleImageView)

 Libraries used in the demo app: 
 - [QuadFlask Color Picker](https://github.com/QuadFlask/colorpicker) 
 - [Bubbleseekbar](https://github.com/woxingxiao/BubbleSeekBar)
 
 

License
-------

    Copyright 2020 Vitor Hugo D. Schwaab

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
