document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("checkoutForm");
    form.addEventListener("submit", function (e) {
        const selectedText = document.getElementById("paymentMethodId")
            .selectedOptions[0].text.trim().toUpperCase();
        if (selectedText === "PAYPAL") {
            e.preventDefault();
            window.location.href = "/orders/paypal/pay";
        }
    });
});
