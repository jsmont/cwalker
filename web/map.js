function initMap() {
    var mapDiv = document.getElementById('map');
    map = new google.maps.Map(mapDiv, {
        center: {lat: 41.3947687, lng: 2.0785561},
        zoom: 12
    });

    google.maps.event.addListener(map, 'click', function(event) {
       $("#buttonDiv").slideDown();
       placeMarker(event.latLng);
       form.location = event.latLng;
    });
    placesService = new google.maps.places.PlacesService(map);
};

