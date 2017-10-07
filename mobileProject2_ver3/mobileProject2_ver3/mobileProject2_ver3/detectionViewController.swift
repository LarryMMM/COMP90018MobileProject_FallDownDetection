//
//  detectionViewController.swift
//  mobileProject2_ver3
//
//  Created by Jianqiang Zhang on 5/10/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit
import CoreMotion
import CoreLocation
//import EventKit
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
    
    @IBOutlet var accX: UILabel!
    @IBOutlet var accY: UILabel!
    @IBOutlet var accZ: UILabel!
    @IBOutlet var accSum: UILabel!
    @IBOutlet var gX: UILabel!
    @IBOutlet var gY: UILabel!
    @IBOutlet var gZ: UILabel!
    
    @IBOutlet var cordsX: UILabel!
    @IBOutlet var cordsY: UILabel!
    
    
    var accX_measure : Double = 0
    var accY_measure : Double = 0
    var accZ_measure : Double = 0
    var accSum_measure : Double = 0
    var gX_measure : Double = 0
    var gY_measure : Double = 0
    var gZ_measure : Double = 0
    
    @IBOutlet var rotX: UILabel!
    @IBOutlet var rotY: UILabel!
    @IBOutlet var rotZ: UILabel!
    var rotX_measure : Double = 0
    var rotY_measure : Double = 0
    var rotZ_measure : Double = 0
    
    let manager = CMMotionManager()
    let locationManager = CLLocationManager()

    
    @IBOutlet var btnDetection: UIButton!
    // Shadow and Radius for Circle Button
    @IBAction func switchDetection(_ sender: Any) {
        if btnDetection.titleLabel?.text == "Start Detection"{
            btnDetection.setTitle("Stop Detection", for: .normal)
            //NotificationCenter.default.addObserver(<#T##observer: Any##Any#>, selector: <#T##Selector#>, name: <#T##NSNotification.Name?#>, object: <#T##Any?#>)
            self.startDetection()
            registerBackgroundTask()
        }else{
            btnDetection.setTitle("Start Detection", for: .normal)
            self.stopDetection()
            endBackgroundTask()
        }
    }
    func startDetection()  {
        self.resetValues()
//        if manager.isAccelerometerAvailable{
//            //print("success in accelerometer")
//            manager.accelerometerUpdateInterval = 0.1 //time interval
//            manager.startAccelerometerUpdates(to: OperationQueue.current!, withHandler: {
//                (accelerometerData: CMAccelerometerData? , NSError)->Void in
//                self.outputAccData(acceleration: accelerometerData!.acceleration)
//                if (NSError != nil){
//                    print("\(String(describing: NSError))")
//                }
//            })
//        }
//        if manager.isGyroAvailable{
//            manager.gyroUpdateInterval = 0.1
//            manager.startGyroUpdates(to: OperationQueue.current!, withHandler: {
//                (gyroData: CMGyroData? , NSError)->Void in
//                self.outputRotData(rotation: gyroData!.rotationRate)
//                if (NSError != nil){
//                    print("\(String(describing: NSError))")
//                }
//            })
//        }
        if manager.isDeviceMotionAvailable{
            manager.deviceMotionUpdateInterval = 0.1
            manager.startDeviceMotionUpdates(to: OperationQueue.current!, withHandler:{
                (deviceData: CMDeviceMotion? , NSError)-> Void in
                self.outputGravityData(gravity: deviceData!.gravity)
                self.outputAccData(acceleration: deviceData!.userAcceleration)
                self.outputRotData(rotation: deviceData!.rotationRate)
                if (NSError != nil){
                    print("\(String(describing: NSError))")
                }
            } )
//            manager.startDeviceMotionUpdates(to: OperationQueue.current!, withHandler: {
//                (userAccelerometer: CMDeviceMotion? , NSError)->Void in
//                self.outputAccData(acceleration: userAccelerometer!.userAcceleration)
//                if (NSError != nil){
//                    print("\(String(describing: NSError))")
//                }
//            })
        }
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
        cordsX?.text = "\(locValue.latitude)"
        cordsY?.text = "\(locValue.longitude)"
        //let region = MKCoordinateRegion(center: locValue, span: MKCoordinateSpan(latitudeDelta: 0.01,longitudeDelta: 0.01))
        //self.map.setRegion(region, animated: true)
        
        //show maps in app
//        let span: MKCoordinateSpan = MKCoordinateSpanMake(0.1, 0.1)
//        let region: MKCoordinateRegion = MKCoordinateRegionMake(locValue, span)
//        map.setRegion(region, animated: true)
//        self.map.showsUserLocation = true
    }
    func stopDetection(){
//        manager.stopAccelerometerUpdates()
//        manager.stopGyroUpdates()
        manager.stopDeviceMotionUpdates()
        self.resetValues()
    }
    
    func resetValues(){
        accX_measure = 0
        accY_measure = 0
        accZ_measure = 0
        accX?.text = "X: 0  g-force"
        accY?.text = "Y: 0  g-force"
        accZ?.text = "Z: 0  g-force"
        accSum.text = "Sum: 0 g-force"
        rotX_measure = 0
        rotY_measure = 0
        rotZ_measure = 0
        rotX?.text = "rotX: 0 rad/s"
        rotY?.text = "rotY: 0 rad/s"
        rotZ?.text = "rotZ: 0 rad/s"
        gX?.text = "gx: 0  g-force"
        gY?.text = "gy: 0  g-force"
        gZ?.text = "gz: 0  g-force"
    }
    func outputRotData(rotation: CMRotationRate) {
        rotX?.text = "rotX:\(round(rotation.x * 1000)/1000) rad/s"
        rotY?.text = "rotY:\(round(rotation.y * 1000)/1000) rad/s"
        rotZ?.text = "rotZ:\(round(rotation.z * 1000)/1000) rad/s"
    }
    func outputAccData(acceleration: CMAcceleration) {
        accX_measure = acceleration.x
        accY_measure = acceleration.y
        accZ_measure = acceleration.z

        accSum_measure = sqrt(pow(accX_measure, 2) + pow(accY_measure, 2) + pow(accZ_measure, 2))
        accX?.text = "X:\(NSString .localizedStringWithFormat("%.4f", accX_measure))  g-force"
        accY?.text = "Y:\(NSString .localizedStringWithFormat("%.4f", accY_measure))  g-force"
        accZ?.text = "Z:\(NSString .localizedStringWithFormat("%.4f", accZ_measure))  g-force"
        accSum?.text = "Sum:\(NSString .localizedStringWithFormat("%.4f", accSum_measure)) g-force"
        print(accSum_measure)
        if (accSum_measure > 0.45 && accSum_measure < 0.9) {
        //if (accSum_measure > 0.2545 && accSum_measure < 0.8) {
        //if (accSum_measure < 0.8) {
            createAlert(title: "Warning", message: "Fall Down Detected!Are you OK?")
            alarmNotification()
        }
    }
    //output gravity
    func outputGravityData(gravity: CMAcceleration) {
        gX_measure = gravity.x
        gY_measure = gravity.y
        gZ_measure = gravity.z
        gX?.text = "gx:\(NSString .localizedStringWithFormat("%.4f", gX_measure)) g-force"
        gY?.text = "gy:\(NSString .localizedStringWithFormat("%.4f", gY_measure)) g-force"
        gZ?.text = "gz:\(NSString .localizedStringWithFormat("%.4f", gZ_measure)) g-force"
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
        //print("test")
        let content = UNMutableNotificationContent()
        content.title = "Fall Down Detected!"
        content.subtitle = "at location \(NSString .localizedStringWithFormat("%.4f", self.cordsX.text!),NSString .localizedStringWithFormat("%.4f", self.cordsY.text!))"
        content.body = "Are You OK?"
        content.badge = 1
        content.sound = UNNotificationSound.default()
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 3 , repeats: false)
        let request = UNNotificationRequest(identifier: "timerDone", content: content, trigger: trigger)
        UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
        let systemSoundID: SystemSoundID = 1005
        //https://github.com/TUNER88/iOSSystemSoundsLibrary
        AudioServicesPlayAlertSound(systemSoundID)
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        btnDetection.layer.shadowColor = UIColor.black.cgColor
        btnDetection.layer.shadowOffset = CGSize(width: 0.0, height: 2.0)
        btnDetection.layer.masksToBounds = false
        btnDetection.layer.shadowRadius = 1.0
        btnDetection.layer.shadowOpacity = 0.5
        btnDetection.layer.cornerRadius = btnDetection.frame.width / 2
        // Do any additional setup after loading the view.
        self.locationManager.requestAlwaysAuthorization()
        self.locationManager.requestWhenInUseAuthorization()
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge], completionHandler: {didallow, error in})
        
        
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
