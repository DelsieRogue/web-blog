const APP_PATH = "/web-blog";
const POST_PATH = APP_PATH + '/post'

function changePageSize() {
    const pageSize = document.getElementById('pageSize').value;
    const tagFilter = document.getElementById('tagFilter').value;
    const currentPage = 1;

    localStorage.setItem('pageSize', pageSize);
    window.location.href = buildPostsUrl(currentPage, pageSize, tagFilter);
}

function changePage(page) {
    const pageSize = localStorage.getItem('pageSize');
    const tagFilter = localStorage.getItem('tagFilter');

    window.location.href = buildPostsUrl(page, pageSize, tagFilter);
}

function applyFilter() {
    const tagFilter = document.getElementById('tagFilter').value;
    const pageSize = document.getElementById('pageSize').value;
    const currentPage = 1;

    localStorage.setItem('tagFilter', tagFilter);
    window.location.href = buildPostsUrl(currentPage, pageSize, tagFilter);
}

function buildPostsUrl(currentPage = 1, pageSize = 10, tagFilter = "") {
    const params = new URLSearchParams();

    if (currentPage !== undefined && currentPage !== null) {
        params.append("page", currentPage);
    }

    if (pageSize !== undefined && pageSize !== null) {
        params.append("size", pageSize);
    }

    if (tagFilter) {
        params.append("tagFilter", encodeURIComponent(tagFilter));
    }

    return `${POST_PATH}?${params.toString()}`;
}

function togglePostForm() {
    const form = document.getElementById('postForm');
    form.style.display = form.style.display === 'none' || form.style.display === '' ? 'block' : 'none';
}

function deletePost(postId) {
    if (confirm("Вы уверены, что хотите удалить этот пост?")) {
        fetch(`${POST_PATH}${postId}`, {
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