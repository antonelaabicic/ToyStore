$(document).on('hidden.bs.modal', '.modal', function () {
    $(this).find('select[id^="quantitySelect__"]').val(1);
});

$(document).on('click', '.btn-add-to-cart', function () {
    const toyId = $(this).data('toy-id');
    const quantity = $(`#quantitySelect__${toyId}`).val() || 1;
    const categoryId = $('#categoryDropdown').val() || '';

    fetch('/cart/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `toyId=${toyId}&quantity=${quantity}&categoryId=${categoryId}`
    }).then(res => {
        if (typeof updateCartCount === 'function') {
            updateCartCount();
        }

        hideToyModal(toyId);
        showToast(toyId, quantity);

    }).catch(err => console.error("Error while fetching:", err));
});

function hideToyModal(toyId) {
    const modalEl = document.getElementById(`toyDetailsModal__${toyId}`);
    if (modalEl) {
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) {
            modal.hide();
        }
    }
}

function showToast(toyId, quantity) {
    const toastEl = document.getElementById('cartToast');
    const toastBody = document.getElementById('cartToastBody');
    const quantityText = quantity > 1 ? ` (${quantity}×)` : '';

    const toyName = document.querySelector(`#toyDetailsModal__${toyId} .modal-body strong + span`)?.textContent?.trim() || 'Item';

    if (toastEl && toastBody) {
        toastBody.textContent = `${toyName}${quantityText} added to cart!`;
        const toast = bootstrap.Toast.getOrCreateInstance(toastEl);
        toast.show();
    }
}
