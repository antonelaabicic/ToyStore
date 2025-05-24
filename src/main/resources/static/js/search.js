function ajaxLoadToysByCategory(categoryId) {
    $('#ajaxCategoryId').val(categoryId);

    $.ajax({
        url: '/store/toys/cards',
        method: 'GET',
        data: { categoryId: categoryId },
        success: function (html) {
            $('#ajaxCardsPlaceholder').html(html);
            $('#searchAndResults').removeClass('d-none');
        },
        error: function () {
            $('#ajaxCardsPlaceholder').html('<div class="text-danger text-center">Error loading toys.</div>');
        }
    });
}

function ajaxSearchToys(e) {
    e.preventDefault();
    const selectedCategoryId = $('#categoryDropdown').val();
    $('#ajaxCategoryId').val(selectedCategoryId);
    const formData = $('#ajaxToySearchForm').serialize();

    $.ajax({
        url: '/store/toys/search',
        method: 'POST',
        data: formData,
        success: function (html) {
            $('#ajaxCardsPlaceholder').html(html);
        },
        error: function () {
            $('#ajaxCardsPlaceholder').html('<div class="text-danger text-center">Search failed.</div>');
        }
    });
}

function ajaxClearFilters() {
    $('#ajaxToySearchForm').find('input[type="text"], input[type="number"]').val('');
    $('#ajaxToySearchForm').submit();
}

function ajaxClearSearch() {
    $('#ajaxToySearchForm')[0].reset();
    $('#categoryDropdown').val('');
    $('#ajaxCategoryId').val('');
    $('#searchAndResults').addClass('d-none');
    $('#ajaxCardsPlaceholder').empty();
}

$(document).ready(function () {
    $(document).on('change', '#categoryDropdown', function () {
        const selectedCategoryId = $(this).val();
        $('#ajaxCategoryId').val(selectedCategoryId);
        if (selectedCategoryId) {
            ajaxLoadToysByCategory(selectedCategoryId);
        } else {
            $('#searchAndResults').addClass('d-none');
            $('#ajaxCardsPlaceholder').empty();
        }
    });

    $(document).on('submit', '#ajaxToySearchForm', ajaxSearchToys);
    $(document).on('click', '#ajaxClearFiltersBtn', function (e) {
        e.preventDefault();
        ajaxClearFilters();
    });
    $(document).on('click', '#ajaxClearSearchBtn', function (e) {
        e.preventDefault();
        ajaxClearSearch();
    });
});