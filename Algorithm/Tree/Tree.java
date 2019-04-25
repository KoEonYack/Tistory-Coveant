public class Tree{


  public void PreOrder(BinaryTreeNode root){
    if(root != null){
      System.out.println(root.data);
      PreOrder(root.left);
      PreOrder(root.right);
    }
  }

  public void InOrder(BinaryTreeNode root){
    if(root != null){
      InOrder(root.left);
      System.out.println(root.data);
      InOrder(root.right);
    }
  }

}
