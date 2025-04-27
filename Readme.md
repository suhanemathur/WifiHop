<h1>WiFiHop - Android App</h1>

<h2>Overview</h2>
<p><strong>WiFiHop</strong> is an Android application designed to scan nearby WiFi access points, collect signal strength data, and analyze it across different locations.
It collects signal strength values 100 times for each selected location (Location A, B, or C) and calculates the range of signal strengths after all locations have been scanned.</p>

<p>This app is useful for basic indoor positioning experiments or WiFi environment analysis.</p>

<h2>Features</h2>
<ul>
  <li>Scan available WiFi networks and pick the strongest access point signal.</li>
  <li>Collect 100 signal strength readings per location.</li>
  <li>Supports scanning for three different locations: Location A, Location B, and Location C.</li>
  <li>Calculates and displays the signal strength range (maximum - minimum) for each location after scanning.</li>
  <li>Displays real-time scanning progress and collected data.</li>
  <li>Handles location permission safely to prevent crashes.</li>
</ul>

<h2>How It Works</h2>
<ol>
  <li>Launch the app and grant location permission when prompted.</li>
  <li>Click on any of the three buttons to start scanning for a specific location.</li>
  <li>The app will automatically collect 100 WiFi signal strength values for that location.</li>
  <li>After scanning all three locations, the app calculates and displays the signal strength range for each location.</li>
</ol>

<h2>Requirements</h2>
<ul>
  <li>Android 6.0 (API 23) or above</li>
  <li>WiFi enabled on the device</li>
  <li>Location permission granted</li>
</ul>

<h2>Permissions Used</h2>
<ul>
  <li><code>ACCESS_FINE_LOCATION</code> — Required to perform WiFi scans as per Android's permissions policy.</li>
</ul>



</body>
</html>