const GOOGLE_API_KEY = "AIzaSyA5MPiWRvthVJ8UTURj0eIm0s4eisFlD-s"

$(document).ready(function(){
    initSlider();

    initList();
});


function initSlider() {
  $("#range").slider({
    range: "min",
    min: 0,
    max: 100,
    value: 50,
    slide: function(e, ui) {
      return $(".ui-slider-handle").html(ui.value);
    }
  });

  $(".ui-slider-handle").html("50");

}

<<<<<<< Updated upstream
var map;
var event_marker;

var form = {
    assistants:[],
    location:{}
};

function initMap() {
    var mapDiv = document.getElementById('map');
    map = new google.maps.Map(mapDiv, {
        center: {lat: 41.3947687, lng: 2.0785561},
        zoom: 12
    });

    google.maps.event.addListener(map, 'click', function(event) {
       placeMarker(event.latLng);
       form.location.lat = event.latLng.lat();
       form.location.lon = event.latLng.lng();
    });
}

function placeMarker(location) {
    if(typeof event_marker == "undefined"){
        event_marker = new google.maps.Marker({
            position: location,
            map: map
        });
    } else {
        event_marker.setPosition(location);
    }
}
=======
function initList(){
    googleRequest(function(googleResuts){
        
    });
}


function googleRequest(cb){
    var service = new google.maps.places.PlacesService(map);
    service.nearbySearch(request, callback);
}
>>>>>>> Stashed changes
