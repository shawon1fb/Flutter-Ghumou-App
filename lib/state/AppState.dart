import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

class AppState with ChangeNotifier {
  static LatLng _initialPosition = LatLng(23.0, 90.0);
  LatLng _lastPosition = _initialPosition;
  bool locationServiceActive = true;
  final Set<Marker> _markers = {};
  final Set<Polyline> _polyLines = {};
  GoogleMapController _mapController;
  TextEditingController locationController = TextEditingController();
  TextEditingController destinationController = TextEditingController();

  LatLng get initialPosition => _initialPosition;

  set initialPosition(LatLng l) => _initialPosition = l;

  LatLng get lastPosition => _lastPosition;

  GoogleMapController get mapController => _mapController;

  Set<Marker> get markers => _markers;

  AppState() {
    getUserLocation();
    // _loadingInitialPosition();
  }

// ! TO GET THE USERS LOCATION
  void getUserLocation() async {
    print("GET USER METHOD RUNNING =========");
    Position position;
    try {
      position = await Geolocator().getCurrentPosition(
          desiredAccuracy: LocationAccuracy.bestForNavigation);
      print("Position >>>>>>>>>>>>>>>>>>>>>>>>>");
      print(position.toString());
    } catch (e) {
      print("execption ========================>> " + e.toString());
      print(e.toString());
    }
    print(position.toString());
    _initialPosition = LatLng(position.latitude, position.longitude);
    /*
    List<Placemark> placemark = await Geolocator()
        .placemarkFromCoordinates(position.latitude, position.longitude);
    
    print(
        "<<=========================================================================>>");
    print(
        "the latitude is: ${position.longitude} and th longitude is: ${position.longitude} ");
    print("initial position is : ${_initialPosition.toString()}");
    locationController.text = placemark[0].name;*/
    _lastPosition = _initialPosition;

    if (_initialPosition != null) {
      MoveCamera_to_location(makeCameraPosition(_initialPosition));
      print('=====camera moved');
    } else {
      print('=====Initial position null');
    }
    notifyListeners();
  }

  // ! SEND REQUEST
  void sendRequest(String intendedLocation) async {
    List<Placemark> placemark =
        await Geolocator().placemarkFromAddress(intendedLocation);
    double latitude = placemark[0].position.latitude;
    double longitude = placemark[0].position.longitude;
    LatLng destination = LatLng(latitude, longitude);
    //_addMarker(destination, intendedLocation);
    /* String route = await _googleMapsServices.getRouteCoordinates(
        _initialPosition, destination);
    createRoute(route);*/
    print("$latitude == $longitude ");
    MoveCamera_to_location(makeCameraPosition(LatLng(latitude, longitude)));
    notifyListeners();
  }

  // ! ADD A MARKER ON THE MAO
  void _addMarker(LatLng location, String address) {
    _markers.clear();
    _markers.add(Marker(
        markerId: MarkerId(_lastPosition.toString()),
        position: location,
        infoWindow: InfoWindow(title: address, snippet: "go here"),
        icon: BitmapDescriptor.defaultMarker));
    notifyListeners();
  }

  // ! ON CAMERA MOVE
  void onCameraMove(CameraPosition position) {
    _lastPosition = position.target;
    notifyListeners();
  }

  // ! ON CREATE
  void onCreated(GoogleMapController controller) {
    _mapController = controller;
    notifyListeners();
  }

  //add marker to point
  void onAddMarkerPressed() {
    _addMarker(_lastPosition, _lastPosition.toString());
  }

  Future<void> MoveCamera_to_location(CameraPosition k) async {
    // final GoogleMapController controller = await _mapController.future;
    _mapController.animateCamera(CameraUpdate.newCameraPosition(k));
  }

  CameraPosition makeCameraPosition(LatLng l) {
    return CameraPosition(
      target: l,
      zoom: 14.4746,
    );
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
  }
}
