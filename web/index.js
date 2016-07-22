const GOOGLE_API_KEY = "AIzaSyA5MPiWRvthVJ8UTURj0eIm0s4eisFlD-s";

var map;
var placesService;
var event_marker;

var form = {
    radius: 1000,
    location:{},
    types: [ 'park', 'amusement_park', 'aquarium', 'art_gallery', 'bar', 'cafe', 'casino', 'cemetery', 'church', 'city_hall', 'hindu_temple', 'museum', 'night_club','place_of_worship', 'rv_park', 'spa', 'stadium', 'synagogue', 'train_station', 'university', 'zoo']
};


$(document).ready(function(){
    initSlider();
    initButtons();
});


function initSlider() {
  $("#range").slider({
    range: "min",
    min: 500,
    max: 10000,
    value: 1000,
    slide: function(e, ui) {
        form.radius = ui.value;
        return $(".ui-slider-handle").html(ui.value + " m");
    }
  });

  $(".ui-slider-handle").html("1000 m");

}
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

function initButtons(){
    $("#searchButton").click(function(){

        resetList();
        fetchAndUpdateList();
    });
}

function resetList(){
    $("#list").html("");
}

function fetchAndUpdateList(){
    googleRequest(function(googleResuts){
        updateList(googleResuts);
    });
}


function googleRequest(cb){

    var request = form; 
    placesService.nearbySearch(request, cb);
}

function updateList(results){
    results
        .map(createBox)
        .map(function(box){
            $("#list").append(box);
            return box;
        })
        .map(fixWidth)
        .map(setBoxEvents);
}

function createBox(elemData){

    var box = $("#box_template .boxedcontainer").first().clone();
    box.find(".nameofbuilding").text(elemData.name);
    box.find(".descriptionofbuilding").text(elemData.vicinity);

    if(typeof elemData.photos != "undefined" && elemData.photos.length != 0){
        box.find(".imatge").css("background-image","url("+elemData.photos[0].getUrl({'maxWidth': 250, 'maxHeight': 250})+")");
    }

    box.show();
    return box;
}

function fixWidth(box){

    var width = box.find(".imatge").width();
    console.log(width);

    box.find(".boxed").css("height",width+"px");
    box.find(".imatge").css("height", width+"px");

    return box;

}

function setBoxEvents(box){
box.click(function(){
    $(this).toggleClass("selected");
}); 

};
