(function ($) {

AjaxSolr.theme.prototype.result = function (doc, snippet) {
  var output = '<div><h2>' + doc.fullNameInEnglishLanguage + '</h2>';
  output += '<p id="links_' + doc.id + '" class="links"></p>';
  output += '<p>' + snippet + '</p></div>';
  return output;
};

AjaxSolr.theme.prototype.snippet = function (doc) {
  var output = '';
  if (doc.fullNameInEnglishLanguage.length > 300) {
    output += doc.dateOfBirth + ' ' + doc.fullNameInEnglishLanguage.substring(0, 300);
    output += '<span style="display:none;">' + doc.fullNameInEnglishLanguage.substring(300);
    output += '</span> <a href="#" class="more">more</a>';
  }
  else {
    output += doc.dateOfBirth + ' ' + doc.fullNameInEnglishLanguage;
  }
  return output;
};

AjaxSolr.theme.prototype.tag = function (value, weight, count, handler) {
  return $('<a href="#" class="tagcloud_item"/>').text(value).append(' (' + count + ') ').addClass('tagcloud_size_' + weight).click(handler);
};

AjaxSolr.theme.prototype.facet_link = function (value, handler) {
  return $('<a href="#"/>').text(value).click(handler);
};

AjaxSolr.theme.prototype.no_items_found = function () {
  return 'no items found in current selection';
};

})(jQuery);
