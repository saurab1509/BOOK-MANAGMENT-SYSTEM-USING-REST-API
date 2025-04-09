document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("bookForm");
    const tableBody = document.querySelector("#bookTable tbody");

    function loadBooks() {
        fetch("http://localhost:8080/RestBookStore/api/books")
            .then(res => res.json())
            .then(data => {
                tableBody.innerHTML = "";
                data.forEach(book => {
                    const row = `<tr>
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td><button onclick="deleteBook(${book.id})">Delete</button></td>
                    </tr>`;
                    tableBody.innerHTML += row;
                });
            });
    }

    form.addEventListener("submit", function (e) {
        e.preventDefault();
        const id = document.getElementById("id").value;
        const title = document.getElementById("title").value;
        const author = document.getElementById("author").value;

        fetch("http://localhost:8080/RestBookStore/api/books", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id, title, author })
        }).then(() => {
            form.reset();
            loadBooks();
        });
    });

    window.deleteBook = function (id) {
        fetch("http://localhost:8080/RestBookStore/api/books/" + id, {
            method: "DELETE"
        }).then(() => loadBooks());
    };

    loadBooks();
});
