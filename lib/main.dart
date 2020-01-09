import 'package:flutter/material.dart';
import 'package:ghumao_app/state/AppState.dart';
import 'package:splashscreen/splashscreen.dart';
import 'Map.dart';
import 'package:provider/provider.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  return runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider.value(
          value: AppState(),
        )
      ],
      child: MaterialApp(

        debugShowCheckedModeBanner: false,
        home: MyApp(),
      ),
    ),
  );
}

/*
void main() {
  runApp(new MaterialApp(
    home: new MyApp(),
  ));
}
*/

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return new SplashScreen(
      seconds: 1,
      navigateAfterSeconds: new AfterSplash(),
      //  image: new Image.asset('images/loading_screen.gif'),

      onClick: () => print("Flutter Egypt"),
      backgroundColor: Color(0xffFCC56B),
      image: Image.asset(
        'images/loading_screen.gif',
        fit: BoxFit.cover,
        height: MediaQuery.of(context).size.height,
        width: MediaQuery.of(context).size.width,
      ),

      imageBackground: AssetImage(
        'images/loading_screen.gif',
      ),
      loaderColor: Color(0xffFCC56B),
    );
  }
}

class AfterSplash extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: MapSample(),
    );
  }
}
