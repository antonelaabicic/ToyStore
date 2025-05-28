document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("toySearchForm");
    const clearBtn = document.getElementById("clearSearchBtn");
    const toysSection = document.querySelector(".row.g-4");

    form.addEventListener("submit", function(event) {
        event.preventDefault();

        const formData = new FormData(form);
        const params = new URLSearchParams();

        for (const [key, value] of formData.entries()) {
            params.append(key, value);
        }

        toysSection.innerHTML = `
            <div class="text-center w-100 my-5">
                <div class="spinner-border text-success" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
            </div>
        `;

        fetch('/admin/toy/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                toysSection.innerHTML = "";

                if (data.length === 0) {
                    toysSection.innerHTML = `<p class="text-center text-muted">No toys found!</p>`;
                } else {
                    data.forEach(toy => {
                        const toyCard = `
                            <div class="col-md-4">
                                <div class="card border-success shadow-sm h-100">
                                    <div class="d-flex justify-content-center align-items-center bg-light" style="height: 200px; overflow: hidden;">
                                        <img src="${toy.imageUrl}" alt="Toy Image" class="img-fluid" style="max-height: 100%; object-fit: contain;">
                                    </div>
                                    <div class="card-body">
                                        <h5 class="card-title text-center">${toy.name}</h5>
                                        <ul class="list-unstyled">
                                            <li><strong>Category:</strong> ${toy.categoryString}</li>
                                            <li><strong>Price:</strong> ${toy.price} EUR</li>
                                            <li class="mt-2">
                                                <p class="text-muted mb-0">${toy.description}</p>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        `;
                        toysSection.insertAdjacentHTML('beforeend', toyCard);
                    });
                }
            })
            .catch(error => {
                console.error('Error during toy search:', error);
                toysSection.innerHTML = `<p class="text-center text-danger">Something went wrong. Please try again.</p>`;
            });
    });

    clearBtn.addEventListener("click", function() {
        form.reset();
        form.dispatchEvent(new Event('submit'));
    });
});
