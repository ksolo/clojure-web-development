$(function(){
  function deleteImages() {
    var selectedInputs = $("input:checked"),
        selectedIds = [];

    selectedInputs.each(function(){
      selectedIds.push($(this).attr("id"));
    });

    if (selectedIds.length < 1) {
      alert("no images selected");
    }
    else {
      $.post(context + "/delete", {names: selectedIds}, function(response){
        var errors = $("<ul>");
        $.each(response, function(){
          if ("ok" === this.status) {
            var element = $("#" + this.name);
            element.parent().parent().remove();
          }
          else {
            errors.append($("<li>", {html: "failed to remove " + this.name + ": " + this.status}));
          }

          if (errors.length > 0) {
            $("#error").empty().append(errors);
          }
        });
      }, "json")
    }
  }

  $("#delete").click(deleteImages);
});
