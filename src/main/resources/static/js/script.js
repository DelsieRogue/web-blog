function togglePostForm() {
    const form = document.getElementById('postForm');
    form.style.display = form.style.display === 'none' || form.style.display === '' ? 'block' : 'none';
}

function deletePost(postId) {
    if (confirm("Вы уверены, что хотите удалить этот пост?")) {
        fetch(`/web-blog/post/${postId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    alert("Произошла ошибка при удалении поста.");
                }
            })
            .catch(error => {
                console.error("Ошибка при удалении поста:", error);
                alert("Произошла ошибка при удалении поста.");
            });
    }
}