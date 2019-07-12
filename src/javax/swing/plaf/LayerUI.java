/*
 * Copyright (c) 2009, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package javax.swing.plaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * The base class for all {@link javax.swing.JLayer}'s UI delegates.
 * <p>
 * {@link #paint(java.awt.Graphics, javax.swing.JComponent)} method performs the
 * painting of the {@code JLayer}
 * and {@link #eventDispatched(AWTEvent, JLayer)} method is notified
 * about any {@code AWTEvent}s which have been generated by a {@code JLayer}
 * or any of its subcomponents.
 * <p>
 * The {@code LayerUI} differs from the UI delegates of the other components,
 * because it is LookAndFeel independent and is not updated by default when
 * the system LookAndFeel is changed.
 * <p>
 * The subclasses of {@code LayerUI} can either be stateless and shareable
 * by multiple {@code JLayer}s or not shareable.
 *
 * @param <V> one of the super types of {@code JLayer}'s view component
 * @author Alexander Potochkin
 * @see JLayer#setUI(LayerUI)
 * @see JLayer#setView(Component)
 * @see JLayer#getView()
 * @since 1.7
 */
public class LayerUI<V extends Component>
    extends ComponentUI implements Serializable {

  private final PropertyChangeSupport propertyChangeSupport =
      new PropertyChangeSupport(this);

  /**
   * Paints the specified component.
   * Subclasses should override this method and use
   * the specified {@code Graphics} object to
   * render the content of the component.
   * <p>
   * The default implementation paints the passed component as is.
   *
   * @param g the {@code Graphics} context in which to paint
   * @param c the component being painted
   */
  public void paint(Graphics g, JComponent c) {
    c.paint(g);
  }

  /**
   * Processes {@code AWTEvent}s for {@code JLayer}
   * and <b>all its descendants</b> to this {@code LayerUI} instance.
   * <p>
   * To enable the {@code AWTEvent}s of a particular type,
   * you call {@link JLayer#setLayerEventMask}
   * in {@link #installUI(javax.swing.JComponent)}
   * and set the layer event mask to {@code 0}
   * in {@link #uninstallUI(javax.swing.JComponent)} after that.
   * By default this  method calls the appropriate
   * {@code process&lt;event&nbsp;type&gt;Event}
   * method for the given class of event.
   * <p>
   * <b>Note:</b> Events are processed only for displayable {@code JLayer}s.
   *
   * @param e the event to be dispatched
   * @param l the layer this LayerUI is set to
   * @see JLayer#setLayerEventMask(long)
   * @see Component#isDisplayable()
   * @see #processComponentEvent
   * @see #processFocusEvent
   * @see #processKeyEvent
   * @see #processMouseEvent
   * @see #processMouseMotionEvent
   * @see #processInputMethodEvent
   * @see #processHierarchyEvent
   * @see #processMouseWheelEvent
   */
  public void eventDispatched(AWTEvent e, JLayer<? extends V> l) {
    if (e instanceof FocusEvent) {
      processFocusEvent((FocusEvent) e, l);

    } else if (e instanceof MouseEvent) {
      switch (e.getID()) {
        case MouseEvent.MOUSE_PRESSED:
        case MouseEvent.MOUSE_RELEASED:
        case MouseEvent.MOUSE_CLICKED:
        case MouseEvent.MOUSE_ENTERED:
        case MouseEvent.MOUSE_EXITED:
          processMouseEvent((MouseEvent) e, l);
          break;
        case MouseEvent.MOUSE_MOVED:
        case MouseEvent.MOUSE_DRAGGED:
          processMouseMotionEvent((MouseEvent) e, l);
          break;
        case MouseEvent.MOUSE_WHEEL:
          processMouseWheelEvent((MouseWheelEvent) e, l);
          break;
      }
    } else if (e instanceof KeyEvent) {
      processKeyEvent((KeyEvent) e, l);
    } else if (e instanceof ComponentEvent) {
      processComponentEvent((ComponentEvent) e, l);
    } else if (e instanceof InputMethodEvent) {
      processInputMethodEvent((InputMethodEvent) e, l);
    } else if (e instanceof HierarchyEvent) {
      switch (e.getID()) {
        case HierarchyEvent.HIERARCHY_CHANGED:
          processHierarchyEvent((HierarchyEvent) e, l);
          break;
        case HierarchyEvent.ANCESTOR_MOVED:
        case HierarchyEvent.ANCESTOR_RESIZED:
          processHierarchyBoundsEvent((HierarchyEvent) e, l);
          break;
      }
    }
  }

  /**
   * Processes component events occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless component events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Component events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.COMPONENT_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code ComponentEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processComponentEvent(ComponentEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes focus events occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless focus events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Focus events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.FOCUS_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code FocusEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processFocusEvent(FocusEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes key events occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless key events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Key events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.KEY_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code KeyEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processKeyEvent(KeyEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes mouse events occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless mouse events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Mouse events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code MouseEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processMouseEvent(MouseEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes mouse motion event occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless mouse motion events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Mouse motion events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.MOUSE_MOTION_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code MouseEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes mouse wheel event occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless mouse wheel events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Mouse wheel events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code MouseEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processMouseWheelEvent(MouseWheelEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes input event occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless input events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Input events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.INPUT_METHOD_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code InputMethodEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processInputMethodEvent(InputMethodEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes hierarchy event occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless hierarchy events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Hierarchy events are enabled in the overridden {@link #installUI} method
   * and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.HIERARCHY_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code HierarchyEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processHierarchyEvent(HierarchyEvent e, JLayer<? extends V> l) {
  }

  /**
   * Processes hierarchy bounds event occurring on the {@link JLayer}
   * or any of its subcomponents.
   * <p>
   * This method is not called unless hierarchy bounds events are
   * enabled for the {@code JLayer} objects, this {@code LayerUI} is set to.
   * Hierarchy bounds events are enabled in the overridden {@link #installUI}
   * method and should be disabled in the {@link #uninstallUI} method after that.
   * <pre>
   * public void installUI(JComponent c) {
   *    super.installUI(c);
   *    JLayer l = (JLayer) c;
   *    l.setLayerEventMask(AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK);
   * }
   *
   * public void uninstallUI(JComponent c) {
   *     super.uninstallUI(c);
   *     JLayer l = (JLayer) c;
   *     l.setLayerEventMask(0);
   * }
   * </pre>
   *
   * @param e the {@code HierarchyEvent} to be processed
   * @param l the layer this {@code LayerUI} instance is set to
   * @see JLayer#setLayerEventMask(long)
   * @see #installUI(javax.swing.JComponent)
   * @see #uninstallUI(javax.swing.JComponent)
   */
  protected void processHierarchyBoundsEvent(HierarchyEvent e, JLayer<? extends V> l) {
  }

  /**
   * Invoked when {@link javax.swing.JLayer#updateUI()} is called
   * by the {@code JLayer} this {@code LayerUI} is set to.
   *
   * @param l the {@code JLayer} which UI is updated
   */
  public void updateUI(JLayer<? extends V> l) {
  }

  /**
   * Configures the {@code JLayer} this {@code LayerUI} is set to.
   * The default implementation registers the passed {@code JLayer} component
   * as a {@code PropertyChangeListener} for the property changes of this {@code LayerUI}.
   *
   * @param c the {@code JLayer} component where this UI delegate is being installed
   */
  public void installUI(JComponent c) {
    addPropertyChangeListener((JLayer) c);
  }

  /**
   * Reverses the configuration which was previously set
   * in the {@link #installUI(JComponent)} method.
   * The default implementation unregisters the passed {@code JLayer} component
   * as a {@code PropertyChangeListener} for the property changes of this {@code LayerUI}.
   *
   * @param c the component from which this UI delegate is being removed.
   */
  public void uninstallUI(JComponent c) {
    removePropertyChangeListener((JLayer) c);
  }

  /**
   * Adds a PropertyChangeListener to the listener list. The listener is
   * registered for all bound properties of this class.
   * <p>
   * If {@code listener} is {@code null},
   * no exception is thrown and no action is performed.
   *
   * @param listener the property change listener to be added
   * @see #removePropertyChangeListener
   * @see #getPropertyChangeListeners
   * @see #addPropertyChangeListener(String, java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Removes a PropertyChangeListener from the listener list. This method
   * should be used to remove PropertyChangeListeners that were registered
   * for all bound properties of this class.
   * <p>
   * If {@code listener} is {@code null},
   * no exception is thrown and no action is performed.
   *
   * @param listener the PropertyChangeListener to be removed
   * @see #addPropertyChangeListener
   * @see #getPropertyChangeListeners
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Returns an array of all the property change listeners
   * registered on this component.
   *
   * @return all of this ui's {@code PropertyChangeListener}s or an empty array if no property
   * change listeners are currently registered
   * @see #addPropertyChangeListener
   * @see #removePropertyChangeListener
   * @see #getPropertyChangeListeners(String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners() {
    return propertyChangeSupport.getPropertyChangeListeners();
  }

  /**
   * Adds a PropertyChangeListener to the listener list for a specific
   * property.
   * <p>
   * If {@code propertyName} or {@code listener} is {@code null},
   * no exception is thrown and no action is taken.
   *
   * @param propertyName one of the property names listed above
   * @param listener the property change listener to be added
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Removes a {@code PropertyChangeListener} from the listener
   * list for a specific property. This method should be used to remove
   * {@code PropertyChangeListener}s
   * that were registered for a specific bound property.
   * <p>
   * If {@code propertyName} or {@code listener} is {@code null},
   * no exception is thrown and no action is taken.
   *
   * @param propertyName a valid property name
   * @param listener the PropertyChangeListener to be removed
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners(String)
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Returns an array of all the listeners which have been associated
   * with the named property.
   *
   * @param propertyName The name of the property being listened to
   * @return all of the {@code PropertyChangeListener}s associated with the named property; if no
   * such listeners have been added or if {@code propertyName} is {@code null}, an empty array is
   * returned
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   * @see #getPropertyChangeListeners
   */
  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    return propertyChangeSupport.getPropertyChangeListeners(propertyName);
  }

  /**
   * Support for reporting bound property changes for Object properties.
   * This method can be called when a bound property has changed and it will
   * send the appropriate PropertyChangeEvent to any registered
   * PropertyChangeListeners.
   *
   * @param propertyName the property whose value has changed
   * @param oldValue the property's previous value
   * @param newValue the property's new value
   */
  protected void firePropertyChange(String propertyName,
      Object oldValue, Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Notifies the {@code LayerUI} when any of its property are changed
   * and enables updating every {@code JLayer}
   * this {@code LayerUI} instance is set to.
   *
   * @param evt the PropertyChangeEvent generated by this {@code LayerUI}
   * @param l the {@code JLayer} this LayerUI is set to
   */
  public void applyPropertyChange(PropertyChangeEvent evt, JLayer<? extends V> l) {
  }

  /**
   * If the {@code JLayer}'s view component is not {@code null},
   * this calls the view's {@code getBaseline()} method.
   * Otherwise, the default implementation is called.
   *
   * @param c {@code JLayer} to return baseline resize behavior for
   * @param width the width to get the baseline for
   * @param height the height to get the baseline for
   * @return baseline or a value &lt; 0 indicating there is no reasonable baseline
   */
  public int getBaseline(JComponent c, int width, int height) {
    JLayer l = (JLayer) c;
    if (l.getView() != null) {
      return l.getView().getBaseline(width, height);
    }
    return super.getBaseline(c, width, height);
  }

  /**
   * If the {@code JLayer}'s view component is not {@code null},
   * this returns the result of the view's {@code getBaselineResizeBehavior()} method.
   * Otherwise, the default implementation is called.
   *
   * @param c {@code JLayer} to return baseline resize behavior for
   * @return an enum indicating how the baseline changes as the component size changes
   */
  public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
    JLayer l = (JLayer) c;
    if (l.getView() != null) {
      return l.getView().getBaselineResizeBehavior();
    }
    return super.getBaselineResizeBehavior(c);
  }

  /**
   * Causes the passed instance of {@code JLayer} to lay out its components.
   *
   * @param l the {@code JLayer} component where this UI delegate is being installed
   */
  public void doLayout(JLayer<? extends V> l) {
    Component view = l.getView();
    if (view != null) {
      view.setBounds(0, 0, l.getWidth(), l.getHeight());
    }
    Component glassPane = l.getGlassPane();
    if (glassPane != null) {
      glassPane.setBounds(0, 0, l.getWidth(), l.getHeight());
    }
  }

  /**
   * If the {@code JLayer}'s view component is not {@code null},
   * this returns the result of  the view's {@code getPreferredSize()} method.
   * Otherwise, the default implementation is used.
   *
   * @param c {@code JLayer} to return preferred size for
   * @return preferred size for the passed {@code JLayer}
   */
  public Dimension getPreferredSize(JComponent c) {
    JLayer l = (JLayer) c;
    Component view = l.getView();
    if (view != null) {
      return view.getPreferredSize();
    }
    return super.getPreferredSize(c);
  }

  /**
   * If the {@code JLayer}'s view component is not {@code null},
   * this returns the result of  the view's {@code getMinimalSize()} method.
   * Otherwise, the default implementation is used.
   *
   * @param c {@code JLayer} to return preferred size for
   * @return minimal size for the passed {@code JLayer}
   */
  public Dimension getMinimumSize(JComponent c) {
    JLayer l = (JLayer) c;
    Component view = l.getView();
    if (view != null) {
      return view.getMinimumSize();
    }
    return super.getMinimumSize(c);
  }

  /**
   * If the {@code JLayer}'s view component is not {@code null},
   * this returns the result of  the view's {@code getMaximumSize()} method.
   * Otherwise, the default implementation is used.
   *
   * @param c {@code JLayer} to return preferred size for
   * @return maximum size for the passed {@code JLayer}
   */
  public Dimension getMaximumSize(JComponent c) {
    JLayer l = (JLayer) c;
    Component view = l.getView();
    if (view != null) {
      return view.getMaximumSize();
    }
    return super.getMaximumSize(c);
  }

  /**
   * Paints the specified region in the {@code JLayer} this {@code LayerUI} is set to, immediately.
   * <p> This method is to be overridden when the dirty region needs to be changed. The default
   * implementation delegates its functionality to {@link JComponent#paintImmediately(int, int, int,
   * int)}.
   *
   * @param x the x value of the region to be painted
   * @param y the y value of the region to be painted
   * @param width the width of the region to be painted
   * @param height the height of the region to be painted
   * @see JComponent#paintImmediately(int, int, int, int)
   */
  public void paintImmediately(int x, int y, int width, int height, JLayer<? extends V> l) {
    l.paintImmediately(x, y, width, height);
  }
}
