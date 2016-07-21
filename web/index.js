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