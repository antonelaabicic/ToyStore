function updateCartCount() {
    fetch('/cart/count')
        .then(res => res.json())
        .then(count => {
            const badge = document.getElementById('cart-count-badge');
            if (!badge) return;

            if (count > 0) {
                badge.style.display = 'inline-block';
                badge.textContent = count;
            } else {
                badge.style.display = 'none';
            }
        })
        .catch(err => console.error("Error fetching cart count:", err));
}

function waitForBadgeAndUpdate() {
    const badge = document.getElementById('cart-count-badge');
    if (badge) {
        updateCartCount();
    } else {
        setTimeout(waitForBadgeAndUpdate, 100);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    waitForBadgeAndUpdate();
});