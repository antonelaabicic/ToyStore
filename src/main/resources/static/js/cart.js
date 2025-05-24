$(document).on('hidden.bs.modal', '.modal', function () {
    $(this).find('select[id^="quantitySelect__"]').val(1);
});

$(document).on('click', '.btn-add-to-cart', function () {
    const toyId = $(this).data('toy-id');
    const quantity = $(`#quantitySelect__${toyId}`).val() || 1;
    const categoryId = $('#categoryDropdown').val() || '';

    console.log(`🧸 Adding toyId=${toyId}, quantity=${quantity}`);

    fetch('/cart/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `toyId=${toyId}&quantity=${quantity}&categoryId=${categoryId}`
    }).then(res => {
        console.log("Response status:", res.status);

        const modalEl = document.getElementById(`toyDetailsModal__${toyId}`);
        if (modalEl) {
            const modal = bootstrap.Modal.getInstance(modalEl);
            if (modal) {
                modal.hide();
            }
        }
    }).catch(err => console.error("Error while fetching:", err));
});