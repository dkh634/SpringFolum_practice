function deletePost(postId){
	if (confirm('本当に削除してよろしいですか？')) {
		window.location.href='/api/delete/'+postId;
	}
}

function replyPost(postId) {
  const textarea = document.getElementById('content');
  if (!textarea) return;

  const replyPrefix = `>>${postId}\n`;

  if (!textarea.value.startsWith(`>> ${postId} `)) {
    // 元のテキストはそのまま残して先頭に追加
    textarea.value += replyPrefix;

    // カーソルはプレフィックスの後（改行の次）にセット
    textarea.selectionStart = textarea.selectionEnd = replyPrefix.length;
  } else {
    textarea.selectionStart = textarea.selectionEnd = textarea.value.length;
  }

  textarea.focus();
}

