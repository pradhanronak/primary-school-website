$(document).ready(function() {
  // Load featured news
  $.get('/api/public/news', function(data) {
    var newsHtml = '';
    data.forEach(function(item) {
      newsHtml += '<div class="news-item">' +
                    '<h4>' + item.title + '</h4>' +
                    '<p>' + item.summary + '</p>' +
                  '</div>';
    });
    $('#news-container').html(newsHtml);
  });

  // Load featured gallery images
  $.get('/api/public/gallery', function(data) {
    var galleryHtml = '';
    data.forEach(function(img) {
      galleryHtml += '<img src="' + img.thumbnailUrl + '" alt="' + img.title + '" />';
    });
    $('#gallery-container').html(galleryHtml);
  });
});
