$(document).ready(function () {
    var data = [[]]
    $("#dataTable").handsontable({
        data: data,
        minSpareRows: 1,
        minRows: 1,
        rowHeaders: true,
        colHeaders: true,
        contextMenu: true
    });
    $.getJSON("load", function (response){
        if (response && response.length > 0)
            $("#dataTable").handsontable("loadData", response)
        else
            $("#dataTable").handsontable("loadData", [[""]])
    })
    $("#save-button").bind("click", function () {
        cells = $("#dataTable").handsontable("getData")
        cells.pop() // 最後空行がはいるっぽい
        $.ajax({
            url: "save",
            data: { "data": cells },
            dataType: "json",
            type: "POST",
            success: function (){
                location.reload()
            }
        });
    })
});
