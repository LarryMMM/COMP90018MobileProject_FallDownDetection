//
//  loginViewController.swift
//  falldown
//
//  Created by Cong on 6/10/17.
//  Copyright © 2017 Microsoft. All rights reserved.
//

import UIKit

class A :Equatable{
    var name:String?
    static func ==(lhs: A, rhs: A) -> Bool{
        return lhs.name == rhs.name
    }
}

extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    func dismissKeyboard() {
        view.endEditing(true)
    }
}

class loginViewController: UIViewController {
    var table : MSTable?

    @IBOutlet weak var userIDTextField: UITextField!
    @IBOutlet weak var userPasswordTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //auto hide keyboard
        self.hideKeyboardWhenTappedAround() 
        
        //connect to client:userData
        let client = MSClient(applicationURLString: "https://falldown.azurewebsites.net")
        table = client.table(withName: "userData")
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func loginButtonTapped(_ sender: Any) {
        let userID = userIDTextField.text
        let userPassword = userPasswordTextField.text        
        
        let predicate = NSPredicate(format:"id == %@", userID!)
        table!.read(with: predicate) { (result, error) in
            if let err = error {
                print("ERROR ", err)
                self.displayMyAlertMessage(userMessage: "Invalid userID")
            } else if let items = result?.items {
                for item in items {
                    print("This ID can be used to login:", item["id"])
                    print("The password is:", item["password"])
                    var getPassword = String(describing: item["password"])
                    let  userPassword = ("Optional(")+userPassword!+(")")
                    print(userPassword)
                    print(getPassword)
                    
                    let classa = A()
                     classa.name = userPassword
                    let classb = A()
                     classb.name = getPassword
                    
                    
                    if classa == classb{
                        print("loging in.....")
                        UserDefaults.standard.set(true, forKey: "isUserLoggedIn")
                        UserDefaults.standard.set(userID, forKey: "loggedInID")
                        UserDefaults.standard.synchronize()
                        
                        self.dismiss(animated: true, completion: nil)
                    }else{
                        self.displayMyAlertMessage(userMessage: "Wrong Username or password")
                    }
                }
            }
        }
        
        
        if(userID?.isEmpty)!{
            displayMyAlertMessage(userMessage: "Please input userID")
        }
        if(userPassword?.isEmpty)!{
            displayMyAlertMessage(userMessage: "Please input password")
        }

    
    }
    
    func displayMyAlertMessage(userMessage:String)
    {
        let myAlert = UIAlertController(title:"Sorry", message:userMessage, preferredStyle: UIAlertControllerStyle.alert);
        
        let okAction = UIAlertAction(title:"Ok", style:UIAlertActionStyle.default, handler:nil);
        
        myAlert.addAction(okAction);
        
        self.present(myAlert, animated:true, completion:nil);
        
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
