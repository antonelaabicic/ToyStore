document.addEventListener("DOMContentLoaded", () => {

    const errorToast =
        document.getElementById("errorToast");

    if (errorToast) {

        bootstrap.Toast
            .getOrCreateInstance(errorToast, {
                delay: 3000
            })
            .show();
    }
});