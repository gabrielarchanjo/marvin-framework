/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.thread;

/**
 * Interface to listen thread events.
 * @author Gabriel Ambrosio Archanjo
 *
 */
public interface MarvinThreadListener {
	public void threadFinished(MarvinThreadEvent a_threadEvent);
}
