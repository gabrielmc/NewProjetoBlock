package physicssimulator;

import interfaces.ICore;
import interfaces.IGameController;
import interfaces.ISimulator;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

public class Box2D implements ISimulator, Runnable, ContactListener{

    private static float PI = 3.14159265359f;
    private static float B2_TIMESTEP = 1.0f / 30.0f;
    private static int B2_VELOCITY_ITERATIONS = 8;
    private static int B2_POSITION_ITERATIONS = 4;
    
    private final ScheduledExecutorService m_scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> m_schedulerHandle;
            
    private static World m_world;
    private ArrayList<Body> m_bodies;
    private Body m_playerBody;
    private Rectangle m_player;
    private Body m_ground;
    private ICore core;
    private HashMap<Body, Rectangle> m_bodyRect;
    
    @Override
    public boolean initialize(ICore core) {
        m_bodyRect = new HashMap<Body, Rectangle>();
        m_bodies = new ArrayList<Body>();
        m_playerBody = null;
        m_ground = null;
        m_schedulerHandle = null;
        
        this.core = core;
        init();
        return true;
    }
    
    @Override
    public void start() {
        m_schedulerHandle = m_scheduler.scheduleAtFixedRate(this, 0, 3, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        m_schedulerHandle.cancel(true);
    }

    @Override
    public void run() {
        m_world.step(B2_TIMESTEP, B2_VELOCITY_ITERATIONS, B2_POSITION_ITERATIONS);
        bodiesUpdated(m_bodies);
    }
    
    @Override
    public void init() {
        m_world = new World(new Vec2(0, -10f), true);
        m_world.setContactListener(this);
        m_bodies.clear();
        
        // Ground
        m_ground = createBody(new Rectangle2D.Float(0.0f, -260.0f, 900.0f, 20.0f), false, 1.0f, 0.3f, 0.5f);

        // Blocks
        int i = 0, j = 0;
        for (i = 0; i < 10; ++i) 
            for (j = 0; j < 11 - i; ++j) {
                m_bodies.add(createBody(new Rectangle2D.Float(-150.0f+15*i+30*j, -236.0f+30*i, 28.0f, 28.0f), true, 1.0f, 0.3f, 0.5f));
            }

        // Player
        j-=2;
        m_bodies.add(m_playerBody = createBody(new Rectangle2D.Float(-150.0f+15*i+30*j, -236.0f+30*i+14, 56.0f, 56.0f), true, 1.0f, 0.3f, 0.5f));
        m_playerBody.setUserData("player");

        bodiesCreated(m_bodies);
    }
    
    public void bodiesCreated(ArrayList<Body> bodies) {
        m_bodyRect.clear();
        Dimension size = core.getUIController().dimensionSize();;
        for (Body body : bodies) {
            Vec2 position = body.getPosition();
            Rectangle rectangle = new Rectangle();
            if (body.getUserData() != null)
            {
                // Player
                rectangle.setRect(-28, -28, 56, 56);
                rectangle.setLocation(size.width/2-28 + (int) position.x, size.height/2-28 - (int) position.y);
                core.getUIController().setM_PLAYER(rectangle);
            }
            else
            {
                // Block
                rectangle.setRect(-14, -14, 28, 28);
                rectangle.setLocation(size.width/2-14 + (int) position.x, size.height/2-14 - (int) position.y);
            }
            m_bodyRect.put(body, rectangle);
        }
        core.getUIController().repaint();
    }
    
    public void bodiesUpdated(ArrayList<Body> bodies) {
        Dimension size = core.getUIController().dimensionSize();
        for (Body body : bodies) {
            Vec2 position = body.getPosition();
            if (body.getUserData() != null)
                // Player
                m_bodyRect.get(body).setLocation(size.width/2-28 + (int) position.x, size.height/2-28 - (int) position.y);
            else
                // Block
                m_bodyRect.get(body).setLocation(size.width/2-14 + (int) position.x, size.height/2-14 - (int) position.y);
        }
        core.getUIController().repaint();
    }

    private Body createBody(Rectangle2D rect, boolean dynamic, float density, float friction, float restitution) {
        BodyDef bodyDef = new BodyDef();
        if (dynamic)
            bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set((float)rect.getX(), (float)rect.getY());
        Body body = m_world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox((float)rect.getWidth()/2, (float)rect.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        body.createFixture(fixtureDef);
        body.setSleepingAllowed(true);
        
        return body;
    }

    public void removeBody(Body body) {
        m_world.destroyBody(body);
        m_bodies.remove(body);
        if (m_bodies.size() == 2)
        {
            stop();
            core.getGameController().setState(IGameController.State.YOUWON);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if ((contact.getFixtureA().getBody() == m_ground && contact.getFixtureB().getBody() == m_playerBody) ||
            (contact.getFixtureB().getBody() == m_ground && contact.getFixtureA().getBody() == m_playerBody))
        {
            stop();
            core.getGameController().setState(IGameController.State.YOULOST);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(core.getGameController().getState() == IGameController.State.RUNNING) {
            Body toBeRemoved = null;
            for (Body body : m_bodyRect.keySet()) {
                java.awt.Rectangle rect = m_bodyRect.get(body);
                if (rect.contains(e.getPoint()) && rect != m_player) {
                    removeBody(body);
                    toBeRemoved = body;
                    break;
                }
            }
            if (toBeRemoved != null)
                m_bodyRect.remove(toBeRemoved);
        }
    }
    
    @Override
    public Collection<Rectangle> getM_BODYRECT() {
        return m_bodyRect.values();
    }
    
    @Override
    public void endContact(Contact cntct) {
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
    
}
