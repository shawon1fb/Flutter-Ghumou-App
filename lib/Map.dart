import 'dart:async';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:ghumao_app/state/AppState.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:provider/provider.dart';

class MapSample extends StatefulWidget {
  @override
  State<MapSample> createState() => MapSampleState();
}

class MapSampleState extends State<MapSample> {
  String address = '';
  final CameraPosition C_position =
      CameraPosition(target: LatLng(23.0, 90.0), zoom: 10.0);

  @override
  Widget build(BuildContext context) {
    final appState = Provider.of<AppState>(context);

    var width = MediaQuery.of(context).size.width;

    return Stack(
      children: <Widget>[
        GoogleMap(
          initialCameraPosition: C_position,
          /* CameraPosition(
                      target: appState.initialPosition, zoom: 10.0),*/
          onMapCreated: appState.onCreated,
          myLocationEnabled: true,
          myLocationButtonEnabled: false,
          mapType: MapType.normal,
          compassEnabled: true,
          markers: appState.markers,
          onCameraMove: appState.onCameraMove,
        ),
        Align(
          alignment: Alignment(0, -0.8),
          child: Container(
            height: 50.0,
            width: width * 0.8,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(3.0),
              color: Colors.white,
              boxShadow: [
                BoxShadow(
                    color: Colors.grey,
                    offset: Offset(1.0, 5.0),
                    blurRadius: 10,
                    spreadRadius: 3)
              ],
            ),
            child: Row(
              children: <Widget>[
                Expanded(
                  child: TextField(
                    onChanged: (v) {
                      address = v;
                    },
                    cursorColor: Colors.black,
                    controller: appState.locationController,
                    decoration: InputDecoration(
                      icon: Container(
                        padding: EdgeInsets.only(left: 5),
                        child: Icon(
                          Icons.location_city,
                          color: Colors.black,
                        ),
                      ),
                      hintText: "Where to go...",
                      border: InputBorder.none,
                      contentPadding: EdgeInsets.only(left: 0, top: 0),
                    ),
                  ),
                ),
                Container(
                  color: Colors.grey,
                  width: 2,
                ),
                Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(3.0),
                  ),
                  child: FlatButton(
                    onPressed: () {
                      if (address != null) appState.sendRequest(address);
                    },
                    child: Icon(Icons.search),
                  ),
                ),
              ],
            ),
          ),
        ),
        /*Positioned(
          top: 105.0,
          right: 15.0,
          left: 15.0,
          child: Container(
            height: 50.0,
            width: double.infinity,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(3.0),
              color: Colors.white,
              boxShadow: [
                BoxShadow(
                    color: Colors.grey,
                    offset: Offset(1.0, 5.0),
                    blurRadius: 10,
                    spreadRadius: 3)
              ],
            ),
            child: TextField(
              cursorColor: Colors.black,
              controller: appState.destinationController,
              textInputAction: TextInputAction.go,
              onSubmitted: (value) {
                appState.sendRequest(value);
              },
              decoration: InputDecoration(
                icon: Container(
                  margin: EdgeInsets.only(left: 20, top: 5),
                  width: 10,
                  height: 10,
                  child: Icon(
                    Icons.local_taxi,
                    color: Colors.black,
                  ),
                ),
                hintText: "destination?",
                border: InputBorder.none,
                contentPadding: EdgeInsets.only(left: 15.0, top: 16.0),
              ),
            ),
          ),
        ),*/
        Align(
          alignment: Alignment(0.9, 0.9),
          child: FloatingActionButton(
            onPressed: appState.getUserLocation,
            tooltip: "aadd marker",
            backgroundColor: Colors.blue,
            child: Icon(
              Icons.location_searching,
              color: Colors.white,
            ),
          ),
        ),
        Align(
          alignment: Alignment(0, 0.9),
          child: Card(
            color: Colors.blue,
            child: InkWell(
              onTap: appState.onAddMarkerPressed,
              child: Container(
                padding: const EdgeInsets.all(10.0),
                /*decoration: BoxDecoration(
                  color: Colors.blue,
                  borderRadius: BorderRadius.circular(5.0),
                ),*/
                child: Text(
                  'Conferm Location',
                  style: TextStyle(
                    //  fontSize: 18.0,
                    color: Colors.white,
                  ),
                ),
              ),
            ),
          ),
        ),
        Align(
          alignment: Alignment.center,
          child: Icon(Icons.add_location),
        ),
      ],
    );
  }
}
