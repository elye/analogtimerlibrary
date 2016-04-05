# Analog Timer Widget for Android
## What It Is
A Timer, that has a single hand and tick as below

![Timer Image](http://static.wixstatic.com/media/d748c3_2a0bc7169e8f4e5283aead6396040476.gif)

## Features / Usage

1. Include `AnalogTimerView` in your layout file.
    ```xml
    <com.elyeproj.analogtimerlibrary.AnalogTimerView
         android:id="@+id/myTimer"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"/>
    ```

2. Instantiate the `AnalogTimerView` in your Activity/Fragment.
    ```
    AnalogTimerView analogTimerView = (AnalogTimerView)findViewById(R.id.myTimer);
    ```

3. Start the Timer by
    ```
    analogTimerView.startTimer();
    ```

4. Stop the Timer by 
    ```
    analogTimerView.stopTimer();
    ```

5. Reset the Timer by 
    ```
    analogTimerView.resetTimer();
    ```

6. You could also set the additional attribute through xml. The below example of what could be set 
    ```xml
    <com.elyeproj.analogtimerlibrary.AnalogTimerView
        android:id="@+id/myTimer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:one_cycle_ticks="60"
        app:period_second="1"
        app:max_time="60"/>
    ```
   * The `one_cycle_ticks` is to indicate how many ticks is in a cycle. The default is 60.
   * The `period_second` is to indicate how many second does one tick takes. Default is 1 second. You could use fraction e.t. 0.5. Value shouldn't be lower than 0.1 though to avoid too much tick that force image refresh within a second.
   * The `max_time` is to indicate a the time will auto stop upon reaching this number of tick. This value should be equal or less than `one period second`

7. Setting Time Out Listener. 
This is useful whenever a timeout happens (i.e. when `max_time` has been achieved), the callback function will be triggered. 
To do this, you could set the TimeOutListener e.g

    ```
    analogTimerView.setTimeOutListener(new AnalogTimerView.TimeOutListener() {
        @Override
        public void onTimeOut() {
            Toast.makeText(getBaseContext(), "Time Out!", Toast.LENGTH_SHORT).show();
        }
    });
    ```

8. For the case of restoring the current state (in the event of Configuration (Orientation) Change or Don't Keep Activity), the library provides 3 APIs below for it.
   1. `int getTime()` - Get the current ticking time. Useful for saveInstanceState.
   2. `void setTime(int time)` - Set the starting ticking time. Useful when restoreInstanceState.
   3. `boolean isRunning()` - Check if it is current running. Use when saveInstanceState, so that when restore instance state, we could decide to start timer automatically or not.


## Requirement
Android SDK API Version 16 and above.

## Importing the Library
On your module `build.gradle`, add

    dependencies {
        compile 'com.elyeproj.libraries:analogtimerlibrary:1.0.0'
    }

## Licence

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.