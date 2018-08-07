# DashboardView

### Preview

<img src="/images/dashboard_preview.gif?raw=true" width="640px">


Easy in use android library for creating dashboard. With this library you
can add the speedometer or tachometer to your app.

## Usage

### Create piggy
Width and Height should be correlated as 4:5
and gravity must be top

```xml
            <com.github.matvapps.piggy.PiggyView
                android:id="@+id/piggy"
                android:layout_width="360dp"
                android:layout_height="450dp"
                android:layout_gravity="top|center_horizontal"/>
```

### In activity
```java
  piggy = (PiggyView) findViewById(R.id.piggy);
```

#### By default, when you init PiggyView obj the piggy already starts blinking animation

<img src="/images/dashboard_preview.gif?raw=true" width="640px">

#### also if you click on the piggy (kick it), then starts crying animation

<img src="/images/dashboard_preview.gif?raw=true" width="640px">

#### and if you stroke the piggy starts joy animation

<img src="/images/dashboard_preview.gif?raw=true" width="640px">

#### You can add money to piggy using:

```java
  piggy.startAddingMoney();
```
<img src="/images/dashboard_preview.gif?raw=true" width="640px">



#### And "crash" piggy, namely, to empty it :
This process more interestring that other,
because for empty piggy user must invert his device, 
and money will be poured from the piggy bank

```java
  piggy.startGetMoney();
```
<img src="/images/dashboard_preview.gif?raw=true" width="640px">



#### Listeners
For easy usage in your bank or piggy bank app i add some listeners

```java
    OnGetMoneyListener
    OnAddMoneyListener
    OnJoyListener
    OnCryListener
```


### Full activity code:
```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PiggyView piggyView;
    
    private Button addMoneyBtn;
    private Button getMoneyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        piggyView = findViewById(R.id.piggy);
        addMoneyBtn = findViewById(R.id.add_money);
        getMoneyBtn = findViewById(R.id.get_money);
        
        addMoneyBtn.setOnClickListener(this);
        getMoneyBtn.setOnClickListener(this);
        
        piggyView.setOnGetMoneyListener(new OnGetMoneyListener() {
            @Override
            public void onStartWaitForDeviceRotate() {
                Toast.makeText(MainActivity.this, "Invert your screen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceRotateNotCompleted() {
                Toast.makeText(MainActivity.this, "Invert not completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStart() {
                Toast.makeText(MainActivity.this, "Beginning of empty the piggy", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStop() {
                Toast.makeText(MainActivity.this, "The piggy is empty", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_money: {
                piggyView.startAddingMoney();
                break;
            }
            case R.id.get_money: {
                piggyView.startGetMoney();
                break;
            }
        }
    }
}
```


### Download

Download via Gradle:

```gradle
  implementation 'com.github.matvapps:Piggy:0.0.1'
```
or Maven:
```xml
<dependency>
  <groupId>com.github.matvapps</groupId>
  <artifactId>Piggy</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
```



Take a look at the [sample project](app) for more information.

### License 

```
Copyright 2017 github.com/matvapps

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


[app]: <https://github.com/matvapps/Piggy/tree/master/app>


