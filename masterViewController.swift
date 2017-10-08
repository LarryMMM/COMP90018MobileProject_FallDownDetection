//
//  masterViewController.swift
//  falldown
//
//  Created by Cong on 6/10/17.
//  Copyright © 2017 Microsoft. All rights reserved.
//

import UIKit

class masterViewController: UIViewController {
    
    @IBOutlet weak var userIDdisplay: UILabel!
    @IBOutlet weak var userNameDisplay: UILabel!
    @IBOutlet weak var userAgeDisplay: UILabel!
    @IBOutlet weak var userPhoneDisplay: UILabel!
    
    var table : MSTable?
    var userID:String = ""
    var userName:String = ""
    var userAge:String = ""
    var userPhone:String = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //auto hide keyboard
        self.hideKeyboardWhenTappedAround()
        
        //hide naviagation bar
        self.navigationController?.isNavigationBarHidden = true
        
        //connect to client:userData
        let client = MSClient(applicationURLString: "https://falldown.azurewebsites.net")
        table = client.table(withName: "userData")
        
        //load user information
//        userID = UserDefaults.standard.string(forKey: "loggedInID")!
//        userIDdisplay.text = ("Hello,") + userID
        
//        let predicate = NSPredicate(format:"userID == %@", userID)
//        table!.read(with: predicate) { (result, error) in
//            if let err = error {
//                print("ERROR ", err)
//            } else if let items = result?.items {
//                for item in items {
//                    self.userNameDisplay.text = item["name"] as? String
//                    self.userAgeDisplay.text = item["Age"] as? String
//                    self.userPhoneDisplay.text = item["phone"] as? String
//                }
//            }
//        }
    }
    

    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.isNavigationBarHidden = true
        
        let isUserLoggedIn = UserDefaults.standard.bool(forKey: "isUserLoggedIn")
        let hadInformation = UserDefaults.standard.bool(forKey: "hadInformation")
        
        if(!isUserLoggedIn){
            self.performSegue(withIdentifier: "loginView", sender: self)
        }else{
            userID = UserDefaults.standard.string(forKey: "loggedInID")!
            userIDdisplay.text = ("Hello,") + userID
            print("MasterPage, userID is:" + userID)
            
            //display user information
            userIDdisplay.text = ("Hello,") + userID
            
            let predicate = NSPredicate(format:"id == %@", userID)
            table!.read(with: predicate) { (result, error) in
                if let err = error {
                    print("ERROR ", err)
                } else if let items = result?.items {
                    for item in items {
                        self.userNameDisplay.text = item["name"] as? String
                        self.userAgeDisplay.text = item["Age"] as? String
                        self.userPhoneDisplay.text = item["phone"] as? String
                    }
                }
            }
        }
        
        if(!hadInformation){
            self.performSegue(withIdentifier: "informationView",sender: self)
        }
    }
    
    @IBAction func logoutButtonTapped(_ sender: Any) {
        UserDefaults.standard.set(false,forKey:"isUserLoggedIn")
        UserDefaults.standard.synchronize()
        self.performSegue(withIdentifier: "loginView", sender: self)
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
