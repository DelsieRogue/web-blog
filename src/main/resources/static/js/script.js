const APP_PATH = "/web-blog";
const POST_PATH = APP_PATH + '/post'
const COMMENT_PATH = APP_PATH + '/comment'
const ENTER_KEY_CODE = 13

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
        fetch(`${POST_PATH}/${postId}`, {
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

function likePost(postId) {
    fetch(`${POST_PATH}/${postId}/like`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Ошибка при добавлении лайка');
            }
        })
        .then(likes => {
            document.getElementById("like-count").textContent = likes;
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Не удалось добавить лайк.');
        });
}

function enableEditing(index) {
    const commentBlock = document.getElementById(`span${index}`);
    const textArea = document.getElementById(`textarea${index}`);

    commentBlock.style.display = 'none';
    textArea.style.display = 'block';
    textArea.value = commentBlock.textContent;
    textArea.focus();
}

function disableEditing(commentBlock, textArea) {
    commentBlock.style.display = 'inline';
    textArea.style.display = 'none';
}

function updateCommentOnBlur(event, index, commentId, postId) {
    const commentBlock = document.getElementById(`span${index}`);
    const textArea = document.getElementById(`textarea${index}`);
    if (commentBlock.textContent !== textArea.value && confirm("Хотите сохранить комментарий?")) {
        updateComment(commentBlock, textArea, commentId, postId)
    } else {
        disableEditing(commentBlock, textArea)
    }
}

function updateCommentKeyUp(event, index, commentId, postId) {
    if (!event.ctrlKey || event.keyCode !== ENTER_KEY_CODE) {
        return
    }
    const commentBlock = document.getElementById(`span${index}`);
    const textArea = document.getElementById(`textarea${index}`);
    if (commentBlock.textContent !== textArea.value) {
        updateComment(commentBlock, textArea, commentId, postId)
    }
}

function updateComment(commentBlock, textArea, commentId, postId) {
    const updateContent = textArea.value;
    fetch(`${COMMENT_PATH}/${commentId}/edit`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({"content": updateContent, "postId": postId})
    })
        .then(response => {
            if (response.ok) {
                textArea.value = updateContent;
                commentBlock.textContent = updateContent;
                disableEditing(commentBlock, textArea)
            } else {
                alert('Ошибка при обновлении комментария.');
            }
        })
        .catch(error => {
            console.error('Ошибка при сохранении комментария:', error);
        });
}

function deleteComment(commentId, index) {
    if (confirm('Вы уверены, что хотите удалить этот комментарий?')) {
        fetch(`${COMMENT_PATH}/${commentId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    const commentBlock = document.getElementById(`li${index}`);
                    commentBlock.remove();
                } else {
                    alert('Ошибка при удалении комментария.');
                }
            })
            .catch(error => {
                console.error('Ошибка при удалении комментария:', error);
            });
    }
}

function addComment(postId) {
    const newCommentField = document.getElementById('newComment');
    const newContent = newCommentField.value;

    if (!newContent.trim()) {
        alert('Комментарий не может быть пустым.');
        return;
    }

    fetch('/web-blog/comment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({"content": newContent, "postId": postId})
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Ошибка при добавлении комментария.');
            }
        })
        .then(commentDtoJson => {
            const commentList = document.querySelector('.comments-list');
            const nextIndex = getNextIndexComment(commentList);
            const commentHtmlBlock = getNextCommentBlock(nextIndex, commentDtoJson["id"], postId, commentDtoJson["content"]);
            commentList.insertAdjacentHTML('beforeend', commentHtmlBlock);
            newCommentField.value = '';
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert(error.message);
        });
}

function getNextCommentBlock(index, commentId, postId, content) {
    return `<li id="li${index}" data-index="${index}" class="comment">
                <span id="span${index}" onclick="enableEditing(${index})" class="comment-text" >${content}</span>
                <textarea id="textarea${index}" class="edit-comment-field" style="display: none;"
                    onkeyup="updateCommentKeyUp(event, ${index}, ${commentId}, ${postId})"
                    onblur="updateCommentOnBlur(event, ${index}, ${commentId}, ${postId})">
                </textarea>
                <button class="delete-comment-button" onclick="deleteComment(${commentId}, ${index})">🗑️</button>
            </li>`;
}

function getNextIndexComment(commentList) {
    const listItems = commentList.querySelectorAll('li');
    let maxIndex = 0;

    listItems.forEach((item) => {
        const index = parseInt(item.getAttribute('data-index'), 10);
        if (index > maxIndex) {
            maxIndex = index;
        }
    });
    return maxIndex + 1;
}