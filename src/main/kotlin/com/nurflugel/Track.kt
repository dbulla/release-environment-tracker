import java.awt.geom.Line2D
import java.awt.geom.AffineTransform
import java.awt.geom.PathIterator

fun main() {
  // Set the starting and ending points for the track
  val x1 = 0.0
  val y1 = 0.0
  var x2 = 10.0
  var y2 = 10.0

  // Set the angle of rotation for the track
  val theta = 45.0

  // Set the distance between each iteration of the track
  val rho = 0.5

  // Create the initial line segment for the track
  val segment = Line2D.Double(x1, y1, x2, y2)

  // Create an ArrayList to store the segments of the track
  val track = arrayListOf<Line2D>()
  track.add(segment)

  // Use a while loop to create additional segments of the track
  while (x2 < 50 && y2 < 50) {
    // Rotate the segment by theta degrees
    //    segment.transform(rotateInstance)
    val pathIterator: PathIterator = segment.getPathIterator(AffineTransform.getRotateInstance(Math.toRadians(theta)))
    //    pathIterator.
    // Translate the segment by rho units
    //    segment.transform(AffineTransform.getTranslateInstance(rho, rho))
    //    segment.getPathIterator(AffineTransform.getTranslateInstance(rho, rho)).next()

    // Get the new ending point of the segment
    x2 = segment.x2
    y2 = segment.y2

    // Add the new segment to the track
    track.add(segment)
  }

  // Print out the segments of the track
  for (line in track) {
    println(line)
  }
}
