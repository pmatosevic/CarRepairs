function showMap(url) {
    var map = L.map('mapid').setView([45.81, 15.98], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    $.getJSON(url, data => {
        for (var i = 0; i < data.length; i++) {
            L.marker(data[i].location).addTo(map)
                .bindPopup(data[i].name)
        }
    });

}
