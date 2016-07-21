$(document).ready(function(){
    initSlider();
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

function initMap() {
        var mapDiv = document.getElementById('map');
        var map = new google.maps.Map(mapDiv, {
            center: {lat: 44.540, lng: -78.546},
            zoom: 8
        });
      }