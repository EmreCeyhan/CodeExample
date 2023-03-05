public class Blackhole extends Actor
{
    /**
     * Method responsible for blackhole collision action.
     * @param A Actor object which has been collided with.
    **/
    public void collisionAction(Actor A)
    {
        if(!A.getClass().toString().equals("class Background")){
            if(A.x < x){ A.setDX(A.getDX()+0.2f); }else{ A.setDX(A.getDX()-0.2f); }
            if(A.y < y){ A.setDY(A.getDY()+0.2f); }else{ A.setDY(A.getDY()-0.2f); }
        }
    }

    public void within(Actor A)
    {
        if( (x + 200 > A.x - (A.bounds.width/2)) && (x - 200 < A.x + (A.bounds.width)/2) ){
            if( (y + 200 > A.y - (A.bounds.height)/2) && (y - 200 < A.y + (A.bounds.height)/2) ){
                if(A.doesCollide()){
                    y -= getDY();
                    x -= getDX();
                }
                collisionAction(A);
            }
        }
    }

    /**
     * Overrites calcMove in Actor, prevents bullets colliding with the screen boundaries.
    **/
    public void calcMove()
    {
        x += getDX();
        y += getDY();
    }

    /**
	 * Blackhole constructor, calls parent constructor.
     * @param x X position.
     * @param y Y position.
    **/
    public Blackhole(int x, int y)
    {
        super(x, y, 0f, 1f, 40, "Data/Graphics/Blackholes/Blackhole.png", false);
    }
}
