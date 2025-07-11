function deletePost(postId){
	if (confirm('本当に削除してよろしいですか？')) {
		window.location.href='/api/delete/'+postId;
	}
}