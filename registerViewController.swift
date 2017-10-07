//
//  registerViewController.swift
//  falldown
//
//  Created by Cong on 5/10/17.
//  Copyright © 2017 Microsoft. All rights reserved.
//

import UIKit
class registerViewController: UIViewController {
    var table : MSTable?
    
    @IBOutlet weak var userIDTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //auto hide keyboard
        self.hideKeyboardWhenTappedAround()
        
        // connect to client
        let client = MSClient(applicationURLString: "https://falldown.azurewebsites.net")
        table = client.table(withName: "userData")
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    

    @IBAction func loginButtonTapped(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    

    @IBAction func registerButtonTapped(_ sender: Any) {
        let userID = userIDTextField.text
        let userPassword = passwordTextField.text
        let confirmPassword = confirmPasswordTextField.text
        

        
        //check userID have registered
        let predicate = NSPredicate(format:"id == %@", userID!)
        table!.read(with: predicate) { (result, error) in
            if let err = error {
                print("ERROR ", err)
                
            } else if let items = result?.items {
                for item in items {
                    
                    let getID = String(describing: item["id"])
                    let userID = ("Optional(") + userID! + (")")
                    let classc = A()
                    classc.name = userID
                    let classd = A()
                    classd.name = getID
                    
                    print(userID)
                    print(getID)
                    
                    if classc != classd{
                        //upload user information
                        let newUser = ["id":userID, "password":userPassword,]
                        self.table!.insert(newUser) { (result, error) in
                            if let err = error {
                                print("ERROR ", err)
                            } else if let item = result {
                                print("new User to register: ", item["userID"])
                            }
                        }
                        
                        //after successful registeration
                        let newAlert = UIAlertController(title:"Notifacation", message:"Registration is done, thank you!", preferredStyle: UIAlertControllerStyle.alert)
                        let finishAction = UIAlertAction(title:"Ok",style:UIAlertActionStyle.default){action in self.dismiss(animated: true, completion:nil)}
                        newAlert.addAction(finishAction)
                        self.present(newAlert,animated: true,completion: nil)

                    }else{
                        self.displayMyAlertMessage(userMessage: "The Email address have been used, change the other one!")
                    }
                }
                let newUser = ["id":userID, "password":userPassword,]
                self.table!.insert(newUser) { (result, error) in
                    if let err = error {
                        print("ERROR ", err)
                    } else if let item = result {
                        print("new User to register: ", item["userID"])
                    }
                }
                
                //after successful registeration
                let newAlert = UIAlertController(title:"Notifacation", message:"Registration is done, thank you!", preferredStyle: UIAlertControllerStyle.alert)
                let finishAction = UIAlertAction(title:"Ok",style:UIAlertActionStyle.default){action in self.dismiss(animated: true, completion:nil)}
                newAlert.addAction(finishAction)
                self.present(newAlert,animated: true,completion: nil)
            }
        }
            
                    
        

        
        
        // Check for empty fields
        if((userID?.isEmpty)! || (userPassword?.isEmpty)! || (confirmPassword?.isEmpty)!)
        {
            displayMyAlertMessage(userMessage: "All fields are required")
            return
        }
        
        //Check if passwords match
        if(userPassword != confirmPassword)
        {
            displayMyAlertMessage(userMessage: "Passwords do not match")
            return
            
        }
        

    }
    
    
    func displayMyAlertMessage(userMessage:String)
    {
        let myAlert = UIAlertController(title:"Sorry", message:userMessage, preferredStyle: UIAlertControllerStyle.alert);
        
        let okAction = UIAlertAction(title:"Ok", style:UIAlertActionStyle.default, handler:nil);
        
        myAlert.addAction(okAction);
        
        self.present(myAlert, animated:true, completion:nil);
        
    }
}

