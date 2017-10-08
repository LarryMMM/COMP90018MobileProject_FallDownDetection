//
//  detectionViewController.swift
//  falldown
//
//  Created by Jianqiang Zhang on 7/10/17.
//  Copyright © 2017 Microsoft. All rights reserved.
//

import UIKit
import CoreMotion
import CoreLocation
import UserNotifications
import AVFoundation

class detectionViewController: UIViewController ,CLLocationManagerDelegate{
    
    var backgroundTask: UIBackgroundTaskIdentifier = UIBackgroundTaskInvalid
    func registerBackgroundTask() {
        backgroundTask = UIApplication.shared.beginBackgroundTask { [weak self] in
            self?.endBackgroundTask()
        }
        assert(backgroundTask != UIBackgroundTaskInvalid)
    }
    
    func endBackgroundTask() {
        print("Background task ended.")
        UIApplication.shared.endBackgroundTask(backgroundTask)
        backgroundTask = UIBackgroundTaskInvalid
    }
  
    var cordsX : Double = 0
    var cordsY : Double = 0
    var accX_measure : Double = 0
    var accY_measure : Double = 0
    var accZ_measure : Double = 0
    var accSum_measure : Double = 0
    var gX_measure : Double = 0
    var gY_measure : Double = 0
    var gZ_measure : Double = 0
    
    let manager = CMMotionManager()
    let locationManager = CLLocationManager()
   
    @IBOutlet var btnDetection: UIButton!
    @IBAction func switchDetection(_ sender: Any) {
        if btnDetection.titleLabel?.text == "Start Detection"{
            btnDetection.setTitle("Stop Detection", for: .normal)
            self.startDetection()
            registerBackgroundTask()
        }else{
            btnDetection.setTitle("Start Detection", for: .normal)
            self.stopDetection()
            endBackgroundTask()
        }
    }

    //detection :updating accelerometer,gravity,rotation,location
    func startDetection()  {
        if manager.isDeviceMotionAvailable{
            manager.deviceMotionUpdateInterval = 0.1
            manager.startDeviceMotionUpdates(to: OperationQueue.current!, withHandler:{
                (deviceData: CMDeviceMotion? , NSError)-> Void in
                self.outputAccData(acceleration: deviceData!.userAcceleration)
                print(deviceData!.userAcceleration)
                if (NSError != nil){
                    print("\(String(describing: NSError))")
                }
            })
        }
        if CLLocationManager.locationServicesEnabled(){
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        }
    }
    
    func outputAccData(acceleration: CMAcceleration) {
        self.accX_measure = acceleration.x
        self.accY_measure = acceleration.y
        self.accZ_measure = acceleration.z
        self.accSum_measure = sqrt(pow(self.accX_measure, 2) + pow(self.accY_measure, 2) + pow(self.accZ_measure, 2))
        print(self.accSum_measure)
        if (self.accSum_measure > 0.45 && self.accSum_measure < 0.9) {
            alarmNotification()
            createAlert(title: "Warning", message: "Fall Down Detected!Are you OK?")
            
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let locValue:CLLocationCoordinate2D = manager.location!.coordinate
        self.cordsX = locValue.latitude
        self.cordsY = locValue.longitude

        
        //let region = MKCoordinateRegion(center: locValue, span: MKCoordinateSpan(latitudeDelta: 0.01,longitudeDelta: 0.01))
        //self.map.setRegion(region, animated: true)
        
        //show maps in app
        //        let span: MKCoordinateSpan = MKCoordinateSpanMake(0.1, 0.1)
        //        let region: MKCoordinateRegion = MKCoordinateRegionMake(locValue, span)
        //        map.setRegion(region, animated: true)
        //        self.map.showsUserLocation = true
    }
    func stopDetection(){
        manager.stopDeviceMotionUpdates()
    }
    
    func  createAlert(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        //create one button
        alert.addAction(UIAlertAction(title: "Yes",style: UIAlertActionStyle.cancel,handler:{(action)in
            alert.dismiss(animated: true, completion: nil)
        }))
        //create another button
        alert.addAction(UIAlertAction(title: "No",style: UIAlertActionStyle.destructive,handler:{(action)in
            alert.dismiss(animated: true, completion: nil)
            
            //should connect Azure and ask for help**
        }))
        self.present(alert, animated: true, completion: nil)
        let when = DispatchTime.now() + 30 //30 seconds for waiting
        DispatchQueue.main.asyncAfter(deadline: when){
            alert.dismiss(animated: true, completion: nil)
            //should connect Azure and ask for help**
        }
    }
    
    func alarmNotification() {
        if #available(iOS 10.0, *) {
            let content = UNMutableNotificationContent()
            content.title = "Fall Down Detected!"
            content.subtitle = "at location \(NSString .localizedStringWithFormat("%.4f", self.cordsX),NSString .localizedStringWithFormat("%.4f", self.cordsY))"
            content.body = "Are You OK?"
            content.badge = 1
            content.sound = UNNotificationSound.default()
            let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 3 , repeats: false)
            let request = UNNotificationRequest(identifier: "timerDone", content: content, trigger: trigger)
            UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
            let systemSoundID: SystemSoundID = 1005
            //https://github.com/TUNER88/iOSSystemSoundsLibrary
            AudioServicesPlayAlertSound(systemSoundID)
        } else {
            // Fallback on earlier versions
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        shadowButton()
        // Do any additional setup after loading the view.
        self.locationManager.requestAlwaysAuthorization()
        self.locationManager.requestWhenInUseAuthorization()
        if #available(iOS 10.0, *) {
            UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge], completionHandler: {didallow, error in})
        } else {
            // Fallback on earlier versions
        }
        
        
    }
    func shadowButton() {
        // Shadow and Radius for Circle Button
        self.btnDetection.layer.shadowColor = UIColor.black.cgColor
        self.btnDetection.layer.shadowOffset = CGSize(width: 0.0, height: 2.0)
        self.btnDetection.layer.masksToBounds = false
        self.btnDetection.layer.shadowRadius = 1.0
        self.btnDetection.layer.shadowOpacity = 0.5
        self.btnDetection.layer.cornerRadius = btnDetection.frame.width / 2
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
