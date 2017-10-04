//
//  LocationViewController.swift
//  Project2-version 2
//
//  Created by Jianqiang Zhang on 30/9/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit

class LocationViewController: UIViewController,CLLocationManagerDelegate {
    @IBOutlet var coordX: UILabel!
    @IBOutlet var coordY: UILabel!
    @IBOutlet var map: MKMapView!
    
    let locationManager = CLLocationManager()
 
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.locationManager.requestAlwaysAuthorization()
        self.locationManager.requestWhenInUseAuthorization()
        if CLLocationManager.locationServicesEnabled(){
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        }
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        //print("test")
        let locValue:CLLocationCoordinate2D = manager.location!.coordinate
        //print("locations = \(locValue.latitude) \(locValue.longitude)")
        coordX?.text = "\(locValue.latitude)"
        coordY?.text = "\(locValue.longitude)"
        //let region = MKCoordinateRegion(center: locValue, span: MKCoordinateSpan(latitudeDelta: 0.01,longitudeDelta: 0.01))
        //self.map.setRegion(region, animated: true)
        let span: MKCoordinateSpan = MKCoordinateSpanMake(0.1, 0.1)
        let region: MKCoordinateRegion = MKCoordinateRegionMake(locValue, span)
        map.setRegion(region, animated: true)
        self.map.showsUserLocation = true
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
